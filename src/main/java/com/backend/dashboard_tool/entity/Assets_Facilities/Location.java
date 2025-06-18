package com.backend.dashboard_tool.entity.Assets_Facilities;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * This class is used to represent the locations.
 * It contains fields for the location name, type, description, and
 * relationships with other entities such as applications and cashflows.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Location {
    /**
     * The unique identifier for the location.
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
    @ManyToMany(mappedBy = "locations")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private List<Asset> assets;

    @JsonIgnore
    @ManyToMany(mappedBy = "locations")
    private List<Application> applications;

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private List<Facility> facilities;

    @JsonIgnore
    @ManyToMany(mappedBy = "locations")
    private List<SystemEntity> systems;
}
