package org.olamide.academicrecordmanagementsystem.service;

import org.olamide.academicrecordmanagementsystem.dto.*;

public interface EnrollmentService {
    Long enroll(EnrollRequest req);
    void drop(Long enrollmentId);
    void completeAndGrade(GradeRequest req);
    BulkOperationResult<Long> enrollMany(BulkEnrollRequest req);
    BulkOperationResult<Long> enrollManyStudents(BulkEnrollStudentsRequest req);

}
