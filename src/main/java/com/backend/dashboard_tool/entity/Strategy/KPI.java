package com.backend.dashboard_tool.entity.Strategy;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent key performance indicators (KPIs).
 * It contains fields for the KPI name, description, and status.
 * It also contains relationships with other entities that need indicators to measure their performance such as processes and goals.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class KPI {
    /**
     * The unique identifier for the KPI.
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
    private String status;

    /**
     * This field is ignored during JSON serialization to prevent circular
     * references. This applies to all the relationships below.
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "kpis")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany(mappedBy = "kpis")
    private List<Goal> goals;

    @JsonIgnore
    @ManyToMany(mappedBy = "kpis")
    private List<Program> programs;

    @JsonIgnore
    @ManyToMany(mappedBy = "kpis")
    private List<Project> projects;
}
