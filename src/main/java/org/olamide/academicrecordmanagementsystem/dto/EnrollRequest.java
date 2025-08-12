package org.olamide.academicrecordmanagementsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.olamide.academicrecordmanagementsystem.enums.Term;

public record EnrollRequest(
        @NotNull Integer studentId,
        @NotNull Integer courseId,
        @NotNull @Min(2000) Integer year,
        @NotNull Term term
) {
}
