package com.backend.dashboard_tool.entity.Strategy;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.backend.dashboard_tool.entity.Document;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is used to represent the legislation. These are the laws and regulations that can trigger processes.
 * It contains fields for the legislation name, description and relationships with 
 * other entities such as processes, audits and cash flows.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Legislation {
    /**
     * The unique identifier for the legislation.
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
    @ManyToMany(mappedBy = "legislations")
    private List<ProcessEntity> processes;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "legislation_audit",
        joinColumns = @JoinColumn(name = "legislation_id"),
        inverseJoinColumns = @JoinColumn(name = "audit_id")
    )
    private List<Audit> audits;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "legislation_document",
        joinColumns = @JoinColumn(name = "legislation_id"),
        inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<Document> documents;
}
