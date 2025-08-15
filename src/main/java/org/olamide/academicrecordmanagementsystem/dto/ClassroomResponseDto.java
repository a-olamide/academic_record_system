package org.olamide.academicrecordmanagementsystem.dto;

import jakarta.persistence.Column;
import lombok.ToString;

public record ClassroomResponseDto(
        Integer id,
        String buildingName,
        String roomNumber
) {
}
