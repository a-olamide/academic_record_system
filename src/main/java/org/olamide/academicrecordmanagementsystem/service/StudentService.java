package org.olamide.academicrecordmanagementsystem.service;

import org.olamide.academicrecordmanagementsystem.dto.CreateStudentRequest;
import org.olamide.academicrecordmanagementsystem.dto.StudentDto;
import org.olamide.academicrecordmanagementsystem.dto.TranscriptDto;
import org.olamide.academicrecordmanagementsystem.dto.UpdateStudentRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentService {
    StudentDto create(CreateStudentRequest req);
    StudentDto getById(Integer id);
    List<StudentDto> list();

    @Transactional
    StudentDto update(Integer id, UpdateStudentRequest patch);

    void delete(Integer id);

    /** (Optional, admin-only) Physical delete AFTER archival/export. */
    void hardDelete(Integer id);
    double recomputeCgpa(Integer studentId);
    TranscriptDto transcript(Integer studentId);
}
