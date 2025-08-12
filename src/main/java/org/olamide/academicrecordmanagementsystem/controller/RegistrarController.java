package org.olamide.academicrecordmanagementsystem.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.olamide.academicrecordmanagementsystem.api.ApiResponse;
import org.olamide.academicrecordmanagementsystem.dto.*;
import org.olamide.academicrecordmanagementsystem.service.*;
import org.olamide.academicrecordmanagementsystem.service.impl.StudentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/registrar")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('REGISTRAR')") // everything here is registrar-only by default
public class RegistrarController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final ClassroomService classroomService;

    // --- Students ---
    @PostMapping("/students")
    public ResponseEntity<ApiResponse<StudentDto>> createStudent(@Valid @RequestBody CreateStudentRequest req) {
        var dto = studentService.create(req);
        return ResponseEntity.status(201).body(ApiResponse.created(dto));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentDto>> getStudent(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getById(id)));
    }

    @GetMapping("/students")
    public ResponseEntity<ApiResponse<java.util.List<StudentDto>>> listStudents() {
        return ResponseEntity.ok(ApiResponse.ok(studentService.list()));
    }

    @PatchMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentDto>> updateStudent(
            @PathVariable Integer id, @Valid @RequestBody UpdateStudentRequest patch) {
        var dto = ((StudentServiceImpl)studentService).update(id, patch);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    // --- Courses ---
    @PostMapping("/courses")
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(@Valid @RequestBody CreateCourseRequest req) {
        var dto = courseService.create(req);
        return ResponseEntity.status(201).body(ApiResponse.created(dto));
    }

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<java.util.List<CourseDto>>> listCourses() {
        return ResponseEntity.ok(ApiResponse.ok(courseService.list()));
    }

    // --- Enrollment: one student -> many courses (bulk) ---
    @PostMapping("/enrollments/bulk")
    public ResponseEntity<ApiResponse<BulkOperationResult<Long>>> bulkEnroll(@Valid @RequestBody BulkEnrollRequest req) {
        var result = enrollmentService.enrollMany(req);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    // --- Enrollment: many students -> one course (bulk) ---
    @PostMapping("/enrollments/bulk-students")
    public ResponseEntity<ApiResponse<BulkOperationResult<Long>>> bulkEnrollStudents(
            @Valid @RequestBody BulkEnrollStudentsRequest req) {
        var result = enrollmentService.enrollManyStudents(req);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    // --- Classroom membership: many students -> one classroom ---
    @PostMapping("/classrooms/assign")
    public ResponseEntity<ApiResponse<BulkOperationResult<Long>>> assignStudentsToClassroom(
            @Valid @RequestBody AddStudentsToClassroomRequest req) {
        var result = classroomService.addStudents(req);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    // Optional: single membership start/end if needed
    @PostMapping("/classrooms/{classroomId}/students/{studentId}/start")
    public ResponseEntity<ApiResponse<Long>> startMembership(
            @PathVariable Integer classroomId, @PathVariable Integer studentId,
            @RequestParam(required = false) java.time.LocalDate startDate) {
        Long id = classroomService.startMembership(studentId, classroomId,
                startDate != null ? startDate : LocalDate.now());
        return ResponseEntity.status(201).body(ApiResponse.created(id));
    }

    @PostMapping("/classrooms/memberships/{membershipId}/end")
    public ResponseEntity<ApiResponse<Void>> endMembership(
            @PathVariable Long membershipId,
            @RequestParam(required = false) java.time.LocalDate endDate) {
        classroomService.endMembership(membershipId, endDate != null ? endDate : LocalDate.now());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }


    @DeleteMapping("/students/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Integer id) {
        studentService.delete(id); // soft delete
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // Optional hard delete (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/students/{id}/hard")
    public ResponseEntity<ApiResponse<Void>> hardDeleteStudent(@PathVariable Integer id) {
        studentService.hardDelete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
