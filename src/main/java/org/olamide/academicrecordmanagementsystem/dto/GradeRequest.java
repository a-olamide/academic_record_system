package org.olamide.academicrecordmanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import org.olamide.academicrecordmanagementsystem.enums.Grade;

public record GradeRequest(
        @NotNull Long enrollmentId,
        @NotNull Grade grade
) {
}
