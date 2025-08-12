package org.olamide.academicrecordmanagementsystem.dto;

import jakarta.validation.constraints.*;
import org.olamide.academicrecordmanagementsystem.enums.Gender;
import org.olamide.academicrecordmanagementsystem.enums.Level;
import org.olamide.academicrecordmanagementsystem.enums.StudentStatus;

import java.time.LocalDate;

public record CreateStudentRequest(
        @NotBlank @Size(max = 32) String studentNumber,
        @NotBlank @Size(max = 100) String firstName,
        @Size(max = 100) String middleName,
        @NotBlank @Size(max = 100) String lastName,

        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 32) String phoneNumber,

        @Past LocalDate dateOfBirth,
        Gender gender,
        @Size(max = 80) String nationality,
        @Size(max = 255) String photoUrl,

        @NotBlank @Size(max = 120) String program,
        @Size(max = 120) String department,
        @NotNull Level level,

        @NotNull LocalDate dateOfEnrollment,
        LocalDate expectedGraduationDate,

        // Optional at creation: default to ACTIVE if null
        StudentStatus status,
        String reasonForExit,
        String remarks
) {
}
