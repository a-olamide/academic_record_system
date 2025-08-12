package org.olamide.academicrecordmanagementsystem.mapper;

import org.mapstruct.*;
import org.olamide.academicrecordmanagementsystem.dto.CreateStudentRequest;
import org.olamide.academicrecordmanagementsystem.dto.StudentDto;
import org.olamide.academicrecordmanagementsystem.dto.UpdateStudentRequest;
import org.olamide.academicrecordmanagementsystem.model.Student;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudentMapper {
    // ---- Entity -> DTO
    StudentDto toDto(Student s);

    // ---- Create DTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "classroomMemberships", ignore = true)
    @Mapping(target = "cgpa", constant = "0.0")
    // default status to ACTIVE when null
    @Mapping(target = "status", expression =
            "java(req.status() != null ? req.status() : StudentStatus.ACTIVE)")
    @Mapping(target = "dateOfGraduation", ignore = true) // set later when status=GRADUATED
    Student toEntity(CreateStudentRequest req);

    // ---- Update (partial) DTO -> existing Entity
    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "middleName", source = "middleName"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phoneNumber", source = "phoneNumber"),
            @Mapping(target = "dateOfBirth", source = "dateOfBirth"),
            @Mapping(target = "gender", source = "gender"),
            @Mapping(target = "nationality", source = "nationality"),
            @Mapping(target = "photoUrl", source = "photoUrl"),
            @Mapping(target = "program", source = "program"),
            @Mapping(target = "department", source = "department"),
            @Mapping(target = "level", source = "level"),
            @Mapping(target = "dateOfEnrollment", source = "dateOfEnrollment"),
            @Mapping(target = "expectedGraduationDate", source = "expectedGraduationDate"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "dateOfGraduation", source = "dateOfGraduation"),
            @Mapping(target = "reasonForExit", source = "reasonForExit"),
            @Mapping(target = "remarks", source = "remarks")
    })
    void updateEntity(@MappingTarget Student target, UpdateStudentRequest patch);
}
