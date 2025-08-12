package org.olamide.academicrecordmanagementsystem.dto;

import jakarta.validation.constraints.*;
import org.olamide.academicrecordmanagementsystem.enums.Gender;
import org.olamide.academicrecordmanagementsystem.enums.Level;
import org.olamide.academicrecordmanagementsystem.enums.StudentStatus;

import java.time.LocalDate;
public record UpdateStudentRequest(
        String firstName,
        String middleName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        Gender gender,
        String nationality,
        String photoUrl,
        String program,
        String department,
        Level level,
        LocalDate dateOfEnrollment,
        LocalDate expectedGraduationDate,
        StudentStatus status,
        LocalDate dateOfGraduation,
        String reasonForExit,
        String remarks
) {
}
