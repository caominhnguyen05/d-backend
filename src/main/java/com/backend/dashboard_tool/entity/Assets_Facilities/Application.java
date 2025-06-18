package com.backend.dashboard_tool.entity.Assets_Facilities;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Finance.Cost;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent the applications associated with various
 * processes.  
 * It contains fields for the application name, type, description, and 
 * relationships with other entities such as processes, locations, and assets.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Application {
    /**
     * The unique identifier for the application.
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
    @ManyToMany(mappedBy = "applications")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "applications_locations",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private List<Location> locations;

    @JsonIgnore
    @ManyToMany(mappedBy = "applications")
    private List<SystemEntity> systems;

    @JsonIgnore
    @ManyToMany(mappedBy = "applications")
    private List<Asset> assets;

    @JsonIgnore
    public Application(String description) {
        this.description = description;
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "applications_costs",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "cost_id")
    )
    private List<Cost> costs;
}