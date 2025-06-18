package com.backend.dashboard_tool.entity.People;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Finance.Cost;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent the employees.
 * It contains fields for the employee name.
 * It also contains the relationships with other entities such as processes, function and organization.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Employee {
    /**
     * The unique identifier for the employee.
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
    @ManyToMany(mappedBy = "employees")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "function_id")
    private Function function;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @JsonIgnore
    @ManyToMany(mappedBy = "employees")
    private List<Role> roles;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "employees_costs",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "cost_id")
    )
    private List<Cost> costs;
}
