package org.olamide.academicrecordmanagementsystem.repository;

import org.olamide.academicrecordmanagementsystem.enums.EnrollmentStatus;
import org.olamide.academicrecordmanagementsystem.model.Enrollment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @EntityGraph(attributePaths = {"course"})
    List<Enrollment> findByStudentIdAndStatus(Integer studentId, EnrollmentStatus status);

    @EntityGraph(attributePaths = {"course"})
    List<Enrollment> findByStudentIdAndStatusIn(Integer studentId, Collection<EnrollmentStatus> statuses);
}
