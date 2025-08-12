package org.olamide.academicrecordmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.olamide.academicrecordmanagementsystem.dto.CourseDto;
import org.olamide.academicrecordmanagementsystem.dto.CreateCourseRequest;
import org.olamide.academicrecordmanagementsystem.exception.ConflictException;
import org.olamide.academicrecordmanagementsystem.exception.NotFoundException;
import org.olamide.academicrecordmanagementsystem.mapper.CourseMapper;
import org.olamide.academicrecordmanagementsystem.repository.CourseRepository;
import org.olamide.academicrecordmanagementsystem.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {
    private final CourseRepository repo;
    private final CourseMapper mapper;

    @Override @Transactional
    public CourseDto create(CreateCourseRequest req) {
        if (repo.existsByCourseCode(req.courseCode()))
            throw new ConflictException("Course code already exists: " + req.courseCode());
        var saved = repo.save(mapper.toEntity(req));
        return mapper.toDto(saved);
    }

    @Override
    public CourseDto getById(Integer id) {
        var c = repo.findById(id).orElseThrow(() -> new NotFoundException("Course not found"));
        return mapper.toDto(c);
    }

    @Override
    public List<CourseDto> list() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }
}
