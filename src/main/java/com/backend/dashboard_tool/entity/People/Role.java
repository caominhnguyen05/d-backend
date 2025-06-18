package com.backend.dashboard_tool.entity.People;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent the roles.
 * It contains fields for the role name.
 * It also contains the relationships with other entities such as processes, function and organizations.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Role {
    /**
     * The unique identifier for the role.
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
    @ManyToMany(mappedBy = "roles")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<Organization> organizations;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<Department> departments;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "roles_employees",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employees;

    @JsonIgnore
    @ManyToOne
    private Function function;
}
