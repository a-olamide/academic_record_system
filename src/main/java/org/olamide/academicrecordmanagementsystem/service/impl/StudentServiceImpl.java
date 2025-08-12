package org.olamide.academicrecordmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.olamide.academicrecordmanagementsystem.dto.*;
import org.olamide.academicrecordmanagementsystem.enums.ClassroomMembershipStatus;
import org.olamide.academicrecordmanagementsystem.enums.EnrollmentStatus;
import org.olamide.academicrecordmanagementsystem.enums.StudentStatus;
import org.olamide.academicrecordmanagementsystem.exception.ConflictException;
import org.olamide.academicrecordmanagementsystem.exception.NotFoundException;
import org.olamide.academicrecordmanagementsystem.mapper.StudentMapper;
import org.olamide.academicrecordmanagementsystem.model.Enrollment;
import org.olamide.academicrecordmanagementsystem.model.Student;
import org.olamide.academicrecordmanagementsystem.repository.EnrollmentRepository;
import org.olamide.academicrecordmanagementsystem.repository.StudentClassroomRepository;
import org.olamide.academicrecordmanagementsystem.repository.StudentRepository;
import org.olamide.academicrecordmanagementsystem.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repo;
    private final EnrollmentRepository enrollRepo;
    private final StudentMapper mapper;
    private final StudentClassroomRepository scRepo;

    @Override @Transactional
    public StudentDto create(CreateStudentRequest req) {
        if (repo.existsByStudentNumber(req.studentNumber()))
            throw new ConflictException("Student number already exists: " + req.studentNumber());
        Student s = mapper.toEntity(req);
        s.setCgpa(0.0);
        return mapper.toDto(repo.save(s));
    }

    @Override
    public StudentDto getById(Integer id) {
        return mapper.toDto(repo.findById(id).orElseThrow(() -> new NotFoundException("Student not found")));
    }

    @Override
    public List<StudentDto> list() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override @Transactional
    public double recomputeCgpa(Integer studentId) {
        var student = repo.findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));
        GpaCreditPoint gpaRec = computeGpa(student.getId());
        student.setCgpa(gpaRec.gpa);
        repo.save(student);
        return gpaRec.gpa;
    }

    @Override
    public TranscriptDto transcript(Integer studentId) {
        var student = repo.findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));
        var completed = enrollRepo.findByStudent_IdAndGradeNotNull(studentId);

        var lines = completed.stream().map(e -> new TranscriptLineDto(
                e.getCourse().getCourseCode(),
                e.getCourse().getTitle(),
                e.getCourse().getCreditHours(),
                e.getGrade(),
                e.getCourse().getCreditHours(),
                e.getTerm(),
                e.getYear()
        )).toList();

        GpaCreditPoint gpaRec = computeGpa(studentId);
        String fullName = (student.getFirstName() + " " +
                (student.getMiddleName() == null ? "" : student.getMiddleName() + " ") +
                student.getLastName()).trim();

        return new TranscriptDto(student.getStudentNumber(), fullName, gpaRec.gpa, gpaRec.totalPoints, lines);
    }

    private GpaCreditPoint computeGpa(Integer studentId) {
        var completed = enrollRepo.findByStudent_IdAndGradeNotNull(studentId);
        double totalPoints = 0.0;
        int totalCredits = 0;
        for (Enrollment e : completed) {
            if (e.getGrade() == null || e.getGrade().points() == null) continue;
            int credits = e.getCourse().getCreditHours();
            totalCredits += credits;
            totalPoints += e.getGrade().points() * credits;
        }
        double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0.0;
        return new GpaCreditPoint(totalCredits,gpa);
    }

    @Transactional
    @Override
    public StudentDto update(Integer id, UpdateStudentRequest patch) {
        var s = repo.findById(id).orElseThrow(() -> new NotFoundException("Student not found"));
        mapper.updateEntity(s, patch);

        // Auto-set graduation date if switching to GRADUATED
        if (patch.status() != null && patch.status() == StudentStatus.GRADUATED && s.getDateOfGraduation() == null) {
            s.setDateOfGraduation(java.time.LocalDate.now());
        }
        return mapper.toDto(repo.save(s));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        var s = repo.findById(id).orElseThrow(() -> new NotFoundException("Student not found"));

        // Guard rails: prevent soft delete if there are ACTIVE enrollments
        boolean hasActiveEnrollments = enrollRepo.findByStudent_IdAndStatus(id, EnrollmentStatus.ENROLLED)
                .stream().anyMatch(e -> e.getStudent().getId().equals(id));
        if (hasActiveEnrollments) {
            throw new ConflictException("Student has active enrollments. Drop or complete them before deletion.");
        }

        // End any ACTIVE classroom memberships gracefully
        var activeMemberships = scRepo.findByStudent_IdAndStatus(id, ClassroomMembershipStatus.ACTIVE);
        activeMemberships.forEach(sc -> { sc.end(java.time.LocalDate.now()); scRepo.save(sc); });

        // Hibernate will run @SQLDelete
        repo.delete(s);
    }

    /** Admin-only, use with extreme caution (breaks history). */
    @Override
    @Transactional
    public void hardDelete(Integer id) {
        var s = repo.findRawById(id).orElseThrow(() -> new NotFoundException("Student not found"));
        // Consider checks again here, then:
        repo.deleteById(id);
    }

    private record GpaCreditPoint(
            int totalPoints,
            double gpa
    ){

    }
}
