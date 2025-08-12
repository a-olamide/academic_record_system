package org.olamide.academicrecordmanagementsystem.dto;

import org.olamide.academicrecordmanagementsystem.enums.Term;

public record BulkEnrollStudentsRequest(
        @jakarta.validation.constraints.NotNull Integer courseId,
        @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.Min(2000) Integer year,
        @jakarta.validation.constraints.NotNull Term term,
        @jakarta.validation.constraints.NotEmpty java.util.List<@jakarta.validation.constraints.NotNull Integer> studentIds

) {
}
