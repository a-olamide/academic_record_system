package org.olamide.academicrecordmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.olamide.academicrecordmanagementsystem.dto.AddStudentsToClassroomRequest;
import org.olamide.academicrecordmanagementsystem.dto.BulkOperationResult;
import org.olamide.academicrecordmanagementsystem.enums.ClassroomMembershipStatus;
import org.olamide.academicrecordmanagementsystem.exception.NotFoundException;
import org.olamide.academicrecordmanagementsystem.model.Student;
import org.olamide.academicrecordmanagementsystem.model.StudentClassroom;
import org.olamide.academicrecordmanagementsystem.repository.ClassroomRepository;
import org.olamide.academicrecordmanagementsystem.repository.StudentClassroomRepository;
import org.olamide.academicrecordmanagementsystem.repository.StudentRepository;
import org.olamide.academicrecordmanagementsystem.service.ClassroomService;
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
public class ClassroomServiceImpl implements ClassroomService {
    private final StudentRepository studentRepo;
    private final ClassroomRepository classroomRepo;
    private final StudentClassroomRepository scRepo;

    @Override
    public Long startMembership(Integer studentId, Integer classroomId, LocalDate startDate) {
        var student = studentRepo.findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));
        var classroom = classroomRepo.findById(classroomId).orElseThrow(() -> new NotFoundException("Classroom not found"));
        var sc = StudentClassroom.start(student, classroom, startDate);
        return scRepo.save(sc).getId();
    }

    @Override
    public void endMembership(Long membershipId, LocalDate endDate) {
        var sc = scRepo.findById(membershipId).orElseThrow(() -> new NotFoundException("Membership not found"));
        sc.end(endDate);
        scRepo.save(sc);
    }
    @Override
    public BulkOperationResult<Long> addStudents(AddStudentsToClassroomRequest req) {
        var classroom = classroomRepo.findById(req.classroomId())
                .orElseThrow(() -> new NotFoundException("Classroom not found"));

        var uniqueStudentIds = req.studentIds().stream().distinct().toList();
        var students = studentRepo.findAllById(uniqueStudentIds);
        var studentById = students.stream().collect(Collectors.toMap(Student::getId, Function.identity()));

        Map<Object, String> failures = new LinkedHashMap<>();
        for (Integer sid : uniqueStudentIds) {
            if (!studentById.containsKey(sid)) {
                failures.put(sid, "Student not found");
            }
        }

        LocalDate start = req.startDate() != null ? req.startDate() : LocalDate.now();
        List<StudentClassroom> toSave = new ArrayList<>();

        for (Integer sid : uniqueStudentIds) {
            if (failures.containsKey(sid)) continue;

            // Prevent duplicate ACTIVE membership for same classroom & student
            var activeForStudent = scRepo.findByStudent_IdAndStatus(sid, ClassroomMembershipStatus.ACTIVE)
                    .stream()
                    .filter(sc -> sc.getClassroom().getId().equals(req.classroomId()))
                    .findFirst();

            if (activeForStudent.isPresent()) {
                failures.put(sid, "Already ACTIVE in this classroom");
                continue;
            }

            var sc = StudentClassroom.start(studentById.get(sid), classroom, start);
            toSave.add(sc);
        }

        var saved = scRepo.saveAll(toSave);
        var successIds = saved.stream().map(StudentClassroom::getId).toList();

        return new BulkOperationResult<>(successIds, failures);
    }
}
