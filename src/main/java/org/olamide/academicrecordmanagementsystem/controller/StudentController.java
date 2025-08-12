package org.olamide.academicrecordmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.olamide.academicrecordmanagementsystem.api.ApiResponse;
import org.olamide.academicrecordmanagementsystem.dto.StudentDto;
import org.olamide.academicrecordmanagementsystem.dto.TranscriptDto;
import org.olamide.academicrecordmanagementsystem.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final StudentService studentService;

    // Example: if username == studentNumber, you can resolve the student's ID from auth
    @GetMapping("/{id}/transcript")
    public ResponseEntity<ApiResponse<TranscriptDto>> transcript(@PathVariable Integer id, Authentication auth) {
        // Optional: enforce that a student can only see their own transcript unless REGISTRAR
        // (Leave as-is for now; wire policy later.)
        var dto = studentService.transcript(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDto>> profile(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getById(id)));
    }
}
