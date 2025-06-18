package com.backend.dashboard_tool.entity.Strategy;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent the audit.
 * It contains fields for the audit name and description.
 * It also contains relationships with other entities such as processes, legislation, and risks.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Audit {
    /**
     * The unique identifier for the audit.
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
    @ManyToMany(mappedBy = "audits")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany(mappedBy = "audits")
    private List<Legislation> legislations;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "audit_risk",
        joinColumns = @JoinColumn(name = "audit_id"),
        inverseJoinColumns = @JoinColumn(name = "risk_id")
    )
    private List<Risk> risks;

    @JsonIgnore
    @ManyToMany(mappedBy = "audits")
    private List<Program> programs;

    @JsonIgnore
    @ManyToMany(mappedBy = "audits")
    private List<Project> projects;
}
