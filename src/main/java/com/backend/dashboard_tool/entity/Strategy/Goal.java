package com.backend.dashboard_tool.entity.Strategy;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent goals associated with processes.
 * It contains information about the goal like name description and type.
 * It also contains relationships with other entities such as products, services, and programs.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Goal {
    /**
     * The unique identifier for the goal.
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
    @ManyToMany(mappedBy = "goals")
    private List<ProcessEntity> processes = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "goal_product",
        joinColumns = @JoinColumn(name = "goal_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "goal_service",
        joinColumns = @JoinColumn(name = "goal_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "goal_program",
        joinColumns = @JoinColumn(name = "goal_id"),
        inverseJoinColumns = @JoinColumn(name = "program_id")
    )
    private List<Program> programs;
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "goal_project",
        joinColumns = @JoinColumn(name = "goal_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "goal_kpi",  
        joinColumns = @JoinColumn(name = "goal_id"),
        inverseJoinColumns = @JoinColumn(name = "kpi_id")
    )
    private List<KPI> kpis;
}
