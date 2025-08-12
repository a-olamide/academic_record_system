package org.olamide.academicrecordmanagementsystem.service;

import org.olamide.academicrecordmanagementsystem.dto.AddStudentsToClassroomRequest;
import org.olamide.academicrecordmanagementsystem.dto.BulkOperationResult;

import java.time.LocalDate;

public interface ClassroomService {
    Long startMembership(Integer studentId, Integer classroomId, LocalDate startDate);
    void endMembership(Long membershipId, LocalDate endDate);
    BulkOperationResult<Long> addStudents(AddStudentsToClassroomRequest req);

}
