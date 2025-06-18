package com.backend.dashboard_tool.entity.Assets_Facilities;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Finance.Cost;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * This class is used to represent the facilities associated with various
 * processes.
 * It contains fields for the facility name, type, description, and
 * relationships with other entities such as processes, systems, and applications.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Facility {
    /**
     * The unique identifier for the facility.
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
    @ManyToMany(mappedBy = "facilities")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToOne
    private Location location;

    @JsonIgnore
    @OneToMany(mappedBy = "facility")
    private List<Asset> assets;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "facilities_systems",
        joinColumns = @JoinColumn(name = "facility_id"),
        inverseJoinColumns = @JoinColumn(name = "system_id")
    )
    private List<SystemEntity> systems;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "facilities_applications",
        joinColumns = @JoinColumn(name = "facility_id"),
        inverseJoinColumns = @JoinColumn(name = "application_id")
    )
    private List<Application> applications;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "facilities_costs",
        joinColumns = @JoinColumn(name = "facility_id"),
        inverseJoinColumns = @JoinColumn(name = "cost_id")
    )
    private List<Cost> costs;
}
