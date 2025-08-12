package org.olamide.academicrecordmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.olamide.academicrecordmanagementsystem.enums.ClassroomMembershipStatus;

import java.time.LocalDate;

@Entity
@Table(name = "student_classrooms",
        indexes = {
                @Index(name="ix_sc_student", columnList="student_id"),
                @Index(name="ix_sc_student_status", columnList="student_id,status")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StudentClassroom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id") @Setter
    @ToString.Exclude
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "classroom_id") @Setter
    @ToString.Exclude
    private Classroom classroom;

    @Column(nullable = false) @Setter @ToString.Include
    private LocalDate startDate;

    @Setter private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Setter @ToString.Include
    private ClassroomMembershipStatus status;

    // convenience transitions
    public static StudentClassroom start(Student s, Classroom c, LocalDate start) {
        return StudentClassroom.builder()
                .student(s)
                .classroom(c)
                .startDate(start != null ? start : LocalDate.now())
                .status(ClassroomMembershipStatus.ACTIVE)
                .build();
    }
    public void end(LocalDate when) {
        this.endDate = (when != null ? when : LocalDate.now());
        this.status = ClassroomMembershipStatus.INACTIVE;
    }
}