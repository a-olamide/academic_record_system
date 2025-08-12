package org.olamide.academicrecordmanagementsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateCourseRequest(
        @NotBlank String courseCode,
        @NotBlank String title,
        @Min(1) Integer creditHours
) {
}
