package com.backend.dashboard_tool.entity.People;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent the assignable functions within an organization.
 * It contains fields for the function name, type, description, and
 * relationships with other entities such as processes, employees and roles.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Function {
    /**
     * The unique identifier for the function.
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
    @ManyToMany(mappedBy = "functions")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @OneToMany(mappedBy = "function")
    private List<Employee> employees;

    @JsonIgnore
    @OneToMany(mappedBy = "function")
    private List<Role> roles;

    @JsonIgnore
    @ManyToMany(mappedBy = "functions")
    private List<Organization> organizations;

    @JsonIgnore
    @ManyToMany(mappedBy = "functions")
    private List<Department> departments;
}
