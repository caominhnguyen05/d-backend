package com.backend.dashboard_tool.entity.Strategy;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Finance.Cost;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent a project.
 * It contains fields for the project name, type, description,
 * and relationships with other entiries such as processes, program, and risks.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Project {
    /**
     * The unique identifier for the project.
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
    private String doel;

    /**
     * This field is ignored during JSON serialization to prevent circular
     * references. This applies to all the relationships below.
     */

    @JsonIgnore
    @ManyToMany(mappedBy = "projects")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany(mappedBy = "projects")
    private List<Goal> goals;

    @JsonIgnore
    @ManyToOne
    private Program program;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "project_risk",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "risk_id")
    )
    private List<Risk> risks;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "project_kpi",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "kpi_id")
    )
    private List<KPI> kpis;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "project_audit",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "audit_id")
    )
    private List<Audit> audits;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "project_cost",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "cost_id")
    )
    private List<Cost> costs;
}
