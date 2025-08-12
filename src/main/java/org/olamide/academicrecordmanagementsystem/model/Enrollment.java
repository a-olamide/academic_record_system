package org.olamide.academicrecordmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.olamide.academicrecordmanagementsystem.enums.EnrollmentStatus;
import org.olamide.academicrecordmanagementsystem.enums.Grade;
import org.olamide.academicrecordmanagementsystem.enums.Term;

import java.time.LocalDate;

@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_enroll_student_course_term",
                columnNames = {"student_id","course_id","year","term"}
        ),
        indexes = {
                @Index(name = "ix_enroll_student", columnList = "student_id"),
                @Index(name = "ix_enroll_course", columnList = "course_id")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Enrollment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    @Setter
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    @Setter
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    @Setter @ToString.Include
    private Term term;

    @Column(nullable = false)
    @Setter @ToString.Include
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    @Setter
    private EnrollmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 12)
    @Setter
    private Grade grade; // null until completed

    @Setter
    private LocalDate completedAt;

    public boolean isCompleted() {
        return status == EnrollmentStatus.COMPLETED && grade != null;
    }
}
