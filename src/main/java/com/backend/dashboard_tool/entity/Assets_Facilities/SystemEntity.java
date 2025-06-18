package com.backend.dashboard_tool.entity.Assets_Facilities;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Finance.Cost;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents the systems saved in the database.
 * This class contains fields for the system name, type, description, and
 * relationships with other entities such as locations, applications, and facilities.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity(name = "system")
public class SystemEntity {
    /**
     * The unique identifier for the system.
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
    @ManyToMany(mappedBy = "systems")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "systems_locations",
        joinColumns = @JoinColumn(name = "system_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private List<Location> locations;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "systems_applications",
        joinColumns = @JoinColumn(name = "system_id"),
        inverseJoinColumns = @JoinColumn(name = "application_id")
    )
    private List<Application> applications;

    @JsonIgnore
    @ManyToMany(mappedBy = "systems")
    private List<Facility> facilities;

    @JsonIgnore
    @ManyToMany(mappedBy = "systems")
    private List<Asset> assets;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "systems_costs",
        joinColumns = @JoinColumn(name = "system_id"),
        inverseJoinColumns = @JoinColumn(name = "cost_id")
    )
    private List<Cost> costs;
}
