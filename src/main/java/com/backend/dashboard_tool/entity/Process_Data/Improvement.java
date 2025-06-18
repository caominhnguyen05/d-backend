package com.backend.dashboard_tool.entity.Process_Data;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents improvents that can/should be made to a process.
 * It can be associated with processes directly or indirectly through problems.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Improvement {
    /**
     * The unique identifier for the improvement.
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
    @ManyToMany(mappedBy = "improvements")
    private List<ProcessEntity> processes = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "improvements")
    private List<Problem> problems = new ArrayList<>();
}
