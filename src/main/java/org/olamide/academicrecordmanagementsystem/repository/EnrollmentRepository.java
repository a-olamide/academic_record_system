package org.olamide.academicrecordmanagementsystem.repository;

import org.olamide.academicrecordmanagementsystem.enums.EnrollmentStatus;
import org.olamide.academicrecordmanagementsystem.enums.Term;
import org.olamide.academicrecordmanagementsystem.model.Enrollment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudent_IdAndCourse_IdAndYearAndTerm(Integer studentId, Integer courseId, Integer year, Term term);
    List<Enrollment> findByStudent_Id(Integer studentId);
    List<Enrollment> findByStudent_IdAndGradeNotNull(Integer studentId);
    List<Enrollment> findByStudent_IdAndStatus(Integer studentId, EnrollmentStatus status);
    List<Enrollment> findByStatus(EnrollmentStatus status);
}
