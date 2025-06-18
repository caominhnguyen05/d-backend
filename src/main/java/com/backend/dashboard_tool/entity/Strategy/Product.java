package com.backend.dashboard_tool.entity.Strategy;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Finance.Revenue;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent the product.
 * It contains fields for the product name, description, and
 * relationships with services and cashflows.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Product {
    /**
     * The unique identifier for the product.
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
    @ManyToMany(mappedBy = "products")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private List<Goal> goals;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "product_service",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "product_revenue",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "revenue_id")
    )
    private List<Revenue> revenues;
}
