package org.olamide.academicrecordmanagementsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.olamide.academicrecordmanagementsystem.enums.Term;

import java.util.List;

public record BulkEnrollRequest(
        @NotNull Integer studentId,
        @NotNull @Min(2000) Integer year,
        @NotNull Term term,
        @NotEmpty List<@NotNull Integer> courseIds
) {
}
