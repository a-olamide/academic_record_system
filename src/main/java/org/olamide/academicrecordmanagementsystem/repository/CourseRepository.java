package org.olamide.academicrecordmanagementsystem.repository;

import org.olamide.academicrecordmanagementsystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByCourseCode(String courseCode);
}
