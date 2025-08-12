package org.olamide.academicrecordmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "students",
        indexes = @Index(name = "ix_students_number", columnList = "studentNumber", unique = true))
public class Student extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 32)
    @ToString.Include
    private String studentNumber;

    @Column(nullable = false) @Setter @ToString.Include
    private String firstName;
    private String middleName;
    @Column(nullable = false) @Setter @ToString.Include
    private String lastName;

    private Double cgpa; // optional cached value; can be derived

    @Column(nullable = false) @Setter
    private LocalDate dateOfEnrollment;

    // Enrollments (owning side is Enrollment)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Enrollment> enrollments = new HashSet<>();

    // Classroom memberships (history)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudentClassroom> classroomMemberships = new HashSet<>();

    // helpers
    public void addEnrollment(Enrollment e) {
        if (e == null) return;
        enrollments.add(e);
        e.setStudent(this);
    }
    public void removeEnrollment(Enrollment e) {
        if (e == null) return;
        enrollments.remove(e);
        e.setStudent(null);
    }
    public void addClassroomMembership(StudentClassroom sc) {
        if (sc == null) return;
        classroomMemberships.add(sc);
        sc.setStudent(this);
    }
}
