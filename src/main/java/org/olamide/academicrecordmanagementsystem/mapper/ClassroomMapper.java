package org.olamide.academicrecordmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.olamide.academicrecordmanagementsystem.dto.ClassroomRequestDto;
import org.olamide.academicrecordmanagementsystem.dto.ClassroomResponseDto;
import org.olamide.academicrecordmanagementsystem.dto.CourseDto;
import org.olamide.academicrecordmanagementsystem.dto.CreateCourseRequest;
import org.olamide.academicrecordmanagementsystem.model.Classroom;
import org.olamide.academicrecordmanagementsystem.model.Course;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClassroomMapper {
    Classroom toEntity(ClassroomRequestDto req);
    ClassroomResponseDto toDto(Classroom c);
}
