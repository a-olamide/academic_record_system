package org.olamide.academicrecordmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.olamide.academicrecordmanagementsystem.dto.*;
import org.olamide.academicrecordmanagementsystem.enums.EnrollmentStatus;
import org.olamide.academicrecordmanagementsystem.exception.ConflictException;
import org.olamide.academicrecordmanagementsystem.exception.NotFoundException;
import org.olamide.academicrecordmanagementsystem.model.Course;
import org.olamide.academicrecordmanagementsystem.model.Enrollment;
import org.olamide.academicrecordmanagementsystem.model.Student;
import org.olamide.academicrecordmanagementsystem.repository.CourseRepository;
import org.olamide.academicrecordmanagementsystem.repository.EnrollmentRepository;
import org.olamide.academicrecordmanagementsystem.repository.StudentRepository;
import org.olamide.academicrecordmanagementsystem.service.EnrollmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository repo;
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;

    @Override
    public Long enroll(EnrollRequest req) {
        var student = studentRepo.findById(req.studentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));
        var course = courseRepo.findById(req.courseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        repo.findByStudent_IdAndCourse_IdAndYearAndTerm(req.studentId(), req.courseId(), req.year(), req.term())
                .ifPresent(e -> { throw new ConflictException("Student already enrolled for this course/term"); });

        var e = Enrollment.builder()
                .student(student)
                .course(course)
                .year(req.year())
                .term(req.term())
                .status(EnrollmentStatus.ENROLLED)
                .build();

        return repo.save(e).getId();
    }

    @Override
    public void drop(Long enrollmentId) {
        var e = repo.findById(enrollmentId).orElseThrow(() -> new NotFoundException("Enrollment not found"));
        e.setStatus(EnrollmentStatus.DROPPED);
        e.setGrade(null);
        repo.save(e);
    }

    @Override
    public void completeAndGrade(GradeRequest req) {
        var e = repo.findById(req.enrollmentId()).orElseThrow(() -> new NotFoundException("Enrollment not found"));
        e.setGrade(req.grade());
        e.setStatus(EnrollmentStatus.COMPLETED);
        e.setCompletedAt(LocalDate.now());
        repo.save(e);
    }
    @Override
    public BulkOperationResult<Long> enrollMany(BulkEnrollRequest req) {
        // Validate parent objects
        var student = studentRepo.findById(req.studentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        // Load all courses in one query
        var uniqueCourseIds = req.courseIds().stream().distinct().toList();
        var courses = courseRepo.findAllById(uniqueCourseIds);
        var courseById = courses.stream().collect(Collectors.toMap(Course::getId, Function.identity()));

        // Detect missing courses
        Map<Object, String> failures = new LinkedHashMap<>();
        for (Integer cid : uniqueCourseIds) {
            if (!courseById.containsKey(cid)) {
                failures.put(cid, "Course not found");
            }
        }

        // For each valid course, check duplicate enrollment and create entities
        List<Enrollment> toSave = new ArrayList<>();
        for (Integer cid : uniqueCourseIds) {
            if (failures.containsKey(cid)) continue; // skip missing

            boolean exists = repo.findByStudent_IdAndCourse_IdAndYearAndTerm(
                    req.studentId(), cid, req.year(), req.term()).isPresent();
            if (exists) {
                failures.put(cid, "Already enrolled for this course/term");
                continue;
            }

            var e = Enrollment.builder()
                    .student(student)
                    .course(courseById.get(cid))
                    .year(req.year())
                    .term(req.term())
                    .status(EnrollmentStatus.ENROLLED)
                    .build();
            toSave.add(e);
        }

        // Batch save
        var saved = repo.saveAll(toSave);
        var successIds = saved.stream().map(Enrollment::getId).toList();

        return new BulkOperationResult<>(successIds, failures);
    }
    @Override
    public BulkOperationResult<Long> enrollManyStudents(BulkEnrollStudentsRequest req) {
        // validate course
        var course = courseRepo.findById(req.courseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        // load students in one go
        var uniqueStudentIds = req.studentIds().stream().distinct().toList();
        var students = studentRepo.findAllById(uniqueStudentIds);
        var studentById = students.stream().collect(java.util.stream.Collectors.toMap(
                Student::getId, java.util.function.Function.identity()));

        java.util.Map<Object,String> failures = new java.util.LinkedHashMap<>();
        for (Integer sid : uniqueStudentIds) {
            if (!studentById.containsKey(sid)) failures.put(sid, "Student not found");
        }

        // build enrollments where not already enrolled
        java.util.List<Enrollment> toSave = new java.util.ArrayList<>();
        for (Integer sid : uniqueStudentIds) {
            if (failures.containsKey(sid)) continue;

            boolean exists = repo.findByStudent_IdAndCourse_IdAndYearAndTerm(
                    sid, req.courseId(), req.year(), req.term()).isPresent();
            if (exists) {
                failures.put(sid, "Already enrolled for this course/term");
                continue;
            }

            var e = Enrollment.builder()
                    .student(studentById.get(sid))
                    .course(course)
                    .year(req.year())
                    .term(req.term())
                    .status(EnrollmentStatus.ENROLLED)
                    .build();
            toSave.add(e);
        }

        var saved = repo.saveAll(toSave);
        var successIds = saved.stream().map(Enrollment::getId).toList();
        return new BulkOperationResult<>(successIds, failures);
    }

}
