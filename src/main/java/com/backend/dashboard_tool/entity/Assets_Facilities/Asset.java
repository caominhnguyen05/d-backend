package com.backend.dashboard_tool.entity.Assets_Facilities;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Finance.Cost;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent the assets associated with various
 * processes.
 * It contains fields for the asset name, type, description, and
 * relationships with other entities such as processes, applications, and cashflows.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Asset {
    /**
     * The unique identifier for the asset.
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
    @ManyToMany(mappedBy = "assets")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "assets_systems",
        joinColumns = @JoinColumn(name = "asset_id"),
        inverseJoinColumns = @JoinColumn(name = "system_id")
    )
    private List<SystemEntity> systems;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "assets_applications",
        joinColumns = @JoinColumn(name = "asset_id"),
        inverseJoinColumns = @JoinColumn(name = "application_id")
    )
    private List<Application> applications;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "assets_costs",
        joinColumns = @JoinColumn(name = "asset_id"),
        inverseJoinColumns = @JoinColumn(name = "cost_id")
    )
    private List<Cost> costs;
}
