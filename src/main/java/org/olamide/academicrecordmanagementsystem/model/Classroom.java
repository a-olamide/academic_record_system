package org.olamide.academicrecordmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "classrooms",
        uniqueConstraints = @UniqueConstraint(name="uq_classrooms_building_room",
                columnNames={"buildingName","roomNumber"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Classroom extends Auditable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false) @ToString.Include
    private String buildingName;

    @Column(nullable = false) @ToString.Include
    private String roomNumber;

    @Builder.Default
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<StudentClassroom> memberships = new HashSet<>();

    public void addMembership(StudentClassroom sc) {
        if (sc == null) return;
        memberships.add(sc);
        sc.setClassroom(this);
    }
}