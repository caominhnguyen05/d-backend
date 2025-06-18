package com.backend.dashboard_tool.entity.Strategy;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Finance.Cost;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent a program.
 * It contains fields for the program name and description.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Program {
    /**
     * The unique identifier for the program.
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
    @ManyToMany(mappedBy = "programs")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany(mappedBy = "programs")
    private List<Goal> goals;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "program_kpi",
        joinColumns = @JoinColumn(name = "program_id"),
        inverseJoinColumns = @JoinColumn(name = "kpi_id")
    )
    private List<KPI> kpis;

    @JsonIgnore
    @OneToMany(mappedBy = "program")
    private List<Project> projects;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "program_risk",
        joinColumns = @JoinColumn(name = "program_id"),
        inverseJoinColumns = @JoinColumn(name = "risk_id")
    )
    private List<Risk> risks;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "program_audit",
        joinColumns = @JoinColumn(name = "program_id"),
        inverseJoinColumns = @JoinColumn(name = "audit_id")
    )
    private List<Audit> audits;
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "program_cost",
        joinColumns = @JoinColumn(name = "program_id"),
        inverseJoinColumns = @JoinColumn(name = "cost_id")
    )
    private List<Cost> costs;
}
