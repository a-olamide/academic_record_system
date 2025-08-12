package org.olamide.academicrecordmanagementsystem.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorCode", description = "Machine-friendly error codes")
public enum ErrorCode {
    VALIDATION_FAILED,
    MALFORMED_JSON,
    MISSING_PARAMETER,
    TYPE_MISMATCH,
    STUDENT_NOT_FOUND,
    RESOURCE_NOT_FOUND,
    DATA_INTEGRITY_VIOLATION,

    UNEXPECTED_ERROR
}
