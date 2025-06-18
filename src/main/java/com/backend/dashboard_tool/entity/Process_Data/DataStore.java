package com.backend.dashboard_tool.entity.Process_Data;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

/**
 * This class is used to represent the datastore, which contain data attributes.
 * It contains fields for the datastore name, type, description,
 * and relationships with processes and data attributes.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class DataStore {
    /**
     * The unique identifier for the datastore.
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
    @ManyToMany(mappedBy = "dataStores")
    private List<ProcessEntity> processes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "dataStore")
    private List<DataAttribute> dataAttributes = new ArrayList<>();
}
