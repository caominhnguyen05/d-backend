package com.backend.dashboard_tool.entity.Process_Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

/**
 * This class is used to represent the data attributes of the dataflows.
 * It contains fields for the data attribute name, type, description and 
 * relationships with dataflow and data store.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class DataAttribute {
    /**
     * The unique identifier for the data attribute.
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
    @ManyToOne
    @JoinColumn(name = "informationflow_id")
    private InformationFlow informationFlow = null;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "data_store_id")
    private DataStore dataStore = null;
}
