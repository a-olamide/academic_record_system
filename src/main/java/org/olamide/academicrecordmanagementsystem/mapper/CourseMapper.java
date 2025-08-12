package org.olamide.academicrecordmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.olamide.academicrecordmanagementsystem.dto.CourseDto;
import org.olamide.academicrecordmanagementsystem.dto.CreateCourseRequest;
import org.olamide.academicrecordmanagementsystem.model.Course;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CourseMapper {
    Course toEntity(CreateCourseRequest req);
    CourseDto toDto(Course c);
}
