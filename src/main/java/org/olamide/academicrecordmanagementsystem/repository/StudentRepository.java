package org.olamide.academicrecordmanagementsystem.repository;

import org.olamide.academicrecordmanagementsystem.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByStudentNumber(String studentNumber);
    boolean existsByStudentNumber(String studentNumber);

    @EntityGraph(attributePaths = {"enrollments", "enrollments.course"})
    Page<Student> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM students WHERE id = :id", nativeQuery = true)
    Optional<Student> findRawById(@Param("id") Integer id); // includes soft-deleted
}
