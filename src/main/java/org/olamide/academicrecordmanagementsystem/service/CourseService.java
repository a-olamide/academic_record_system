package org.olamide.academicrecordmanagementsystem.service;

import org.olamide.academicrecordmanagementsystem.dto.CourseDto;
import org.olamide.academicrecordmanagementsystem.dto.CreateCourseRequest;

import java.util.List;

public interface CourseService {
    CourseDto create(CreateCourseRequest req);
    CourseDto getById(Integer id);
    List<CourseDto> list();
}
