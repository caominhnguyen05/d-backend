package com.backend.dashboard_tool.entity.Strategy;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Finance.Revenue;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent a service.
 * It contains fields for the service name and description.
 * It has relations with the associated processes and cashflows.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Service {
    /**
     * The unique identifier for the service.
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
    @ManyToMany(mappedBy = "services")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany(mappedBy = "services")
    private List<Goal> goals;

    @JsonIgnore
    @ManyToMany(mappedBy = "services")
    private List<Product> products;
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "service_revenue",
        joinColumns = @JoinColumn(name = "service_id"),
        inverseJoinColumns = @JoinColumn(name = "revenue_id")
    )
    private List<Revenue> revenues;
}
