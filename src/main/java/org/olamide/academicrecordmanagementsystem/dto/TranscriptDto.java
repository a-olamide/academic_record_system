package org.olamide.academicrecordmanagementsystem.dto;

import java.util.List;

public record TranscriptDto(
        String studentNumber,
        String studentName,
        double gpa,
        int totalCredits,
        List<TranscriptLineDto> lines
) {
}
