package org.olamide.academicrecordmanagementsystem.service;

import org.olamide.academicrecordmanagementsystem.dto.AddStudentsToClassroomRequest;
import org.olamide.academicrecordmanagementsystem.dto.BulkOperationResult;
import org.olamide.academicrecordmanagementsystem.dto.ClassroomRequestDto;
import org.olamide.academicrecordmanagementsystem.dto.ClassroomResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ClassroomService {
    Long startMembership(Integer studentId, Integer classroomId, LocalDate startDate);
    void endMembership(Long membershipId, LocalDate endDate);
    BulkOperationResult<Long> addStudents(AddStudentsToClassroomRequest req);
    ClassroomResponseDto createClassroom(ClassroomRequestDto classroomRequestDto);
    List<ClassroomResponseDto> getAllClassrooms();
    ClassroomResponseDto getClassroomById(Integer classroomId);
}
