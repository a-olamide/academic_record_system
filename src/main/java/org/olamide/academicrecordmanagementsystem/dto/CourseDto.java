package org.olamide.academicrecordmanagementsystem.dto;

public record CourseDto(
        Integer id,
        String courseCode,
        String title,
        Integer creditHours
) {
}
