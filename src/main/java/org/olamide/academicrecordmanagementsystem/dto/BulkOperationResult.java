package org.olamide.academicrecordmanagementsystem.dto;

import java.util.List;
import java.util.Map;

public record BulkOperationResult<ID>(
        List<ID> successIds,           // created/affected entity IDs
        Map<Object, String> failures
) {
}
