package org.olamide.academicrecordmanagementsystem.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record AddStudentsToClassroomRequest(
        @NotNull Integer classroomId,
        @NotEmpty List<@NotNull Integer> studentIds,
        LocalDate startDate // nullable -> defaults to today
) {
}
