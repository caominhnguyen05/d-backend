package com.backend.dashboard_tool.entity.Process_Data;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

/**
 * This class is used to represent problemS associated with processes.
 * It contains information about the problem and can have improvements associated with it.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Problem {
    /**
     * The unique identifier for the problem.
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
    @ManyToMany(mappedBy = "problems")
    private List<ProcessEntity> processes = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "problem_improvement",
        joinColumns = @JoinColumn(name = "problem_id"),
        inverseJoinColumns = @JoinColumn(name = "improvement_id")
    )
    private List<Improvement> improvements = new ArrayList<>();
}
