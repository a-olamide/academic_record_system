package org.olamide.academicrecordmanagementsystem.repository;

import org.olamide.academicrecordmanagementsystem.model.StudentClassroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentClassroomRepository extends JpaRepository<StudentClassroom, Long> {
    List<StudentClassroom> findByStudentId(Integer studentId);
}
