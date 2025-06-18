package com.backend.dashboard_tool.entity.Strategy;

import java.util.List;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used to represent a risk.
 * It contains fields for the risk name, description, and status.
 * It can have chance or threats related to it.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Risk {
    /**
     * The unique identifier for the risk.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String cause;
    private String consequence;

    private Double grossImpact;
    private Double netImpact;

    private String warning;

    private Double estimate;

    private String estimateExplanation;
    private String strategy;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    private Boolean internal;
    private Integer level;

    // private String type;
    // private String soort;
    // private String status;

    /**
     * This field is ignored during JSON serialization to prevent circular
     * references. This applies to all the relationships below.
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "risks")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany(mappedBy = "risks")
    private List<Program> programs;

    @JsonIgnore
    @ManyToMany(mappedBy = "risks")
    private List<Project> projects;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
      name = "risk_threat",
      joinColumns = @JoinColumn(name = "risk_id"),
      inverseJoinColumns = @JoinColumn(name = "threat_id")
    )
    private List<Threat> threats;

    @JsonIgnore
    @ManyToMany(mappedBy = "risks")
    private List<MitigationMeasure> mitigation_measures;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
      name = "risk_chance",
      joinColumns = @JoinColumn(name = "risk_id"),
      inverseJoinColumns = @JoinColumn(name = "chance_id")
    )
    private List<Chance> chances;

    @JsonIgnore
    @ManyToMany(mappedBy = "risks")
    private List<Audit> audits;
}
