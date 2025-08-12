package org.olamide.academicrecordmanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.olamide.academicrecordmanagementsystem.api.ApiResponse;
import org.olamide.academicrecordmanagementsystem.dto.GradeRequest;
import org.olamide.academicrecordmanagementsystem.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/faculty")
@RequiredArgsConstructor
@PreAuthorize("hasRole('FACULTY')")
public class FacultyController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/grade")
    public ResponseEntity<ApiResponse<Void>> grade(@Valid @RequestBody GradeRequest req) {
        enrollmentService.completeAndGrade(req);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
