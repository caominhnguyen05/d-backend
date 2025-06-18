package com.backend.dashboard_tool.entity.People;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent the consultations within an organization.
 * It contains fields for the consultation name, type, description, and
 * relationships with other entities such as processess and departments.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Consultation {
    /**
     * The unique identifier for the consultation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String soort;
    private Boolean internal;
    private Integer level;
    private String description;

    /**
	 * This field is ignored during JSON serialization to prevent circular
	 * references. This applies to all the relationships below.
	 */
    @JsonIgnore
    @ManyToMany(mappedBy = "consultations")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany(mappedBy = "consultations")
    private List<Department> departments;
}
