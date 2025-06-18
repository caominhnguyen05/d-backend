package com.backend.dashboard_tool.entity.Strategy;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent mitigation measures.
 * It contains fields for the mitigation measure name and description.
 * It has a relationship with processes and risks.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class MitigationMeasure {
    /**
     * The unique identifier for the mitigation measure.
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
    @ManyToMany(mappedBy = "mitigationMeasures")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "mitigation_measure_risk",
        joinColumns = @JoinColumn(name = "mitigation_measure_id"),
        inverseJoinColumns = @JoinColumn(name = "risk_id")
    )
    private List<Risk> risks;
}
