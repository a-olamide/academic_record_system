package org.olamide.academicrecordmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses",
        indexes = @Index(name = "ix_courses_code", columnList = "courseCode", unique = true))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Course extends Auditable  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false, unique = true, length = 16)
    @ToString.Include
    private String courseCode;

    @Column(nullable = false) @Setter @ToString.Include
    private String title;

    @Column(nullable = false) @Setter
    private Integer creditHours;

    @Builder.Default
    @OneToMany(mappedBy = "course")
    @ToString.Exclude
    private Set<Enrollment> enrollments = new HashSet<>();
}
