package com.backend.dashboard_tool.entity.People;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Finance.Cost;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent the departments within an organization.
 * It contains fields for the department name, type, description, and
 * relationships with other entities such as employees, organization, and cashflows.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Department {
    /**
     * The unique identifier for the department.
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
    @ManyToMany(mappedBy = "departments")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "departments_roles",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "departments_functions",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "function_id")
    )
    private List<Function> functions;

    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    @JsonIgnore
    @ManyToOne
    private Organization organization;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "departments_costs",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "cost_id")
    )
    private List<Cost> costs;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "departments_consultations",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "consultation_id")
    )
    private List<Consultation> consultations;
}
