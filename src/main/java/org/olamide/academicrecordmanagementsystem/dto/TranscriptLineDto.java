package org.olamide.academicrecordmanagementsystem.dto;

import org.olamide.academicrecordmanagementsystem.enums.Grade;
import org.olamide.academicrecordmanagementsystem.enums.Term;

public record TranscriptLineDto(
        String courseCode,
        String courseTitle,
        int creditHours,
        Grade grade,
        double gradePoints,   // creditHours * grade.points()
        Term term,
        int year
) {
}
