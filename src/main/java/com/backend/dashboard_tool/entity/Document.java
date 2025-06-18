package com.backend.dashboard_tool.entity;

import java.time.LocalDate;
import java.util.List;

import com.backend.dashboard_tool.entity.Strategy.Legislation;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used to represent the documents.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Document {
    /**
     * The unique identifier for the document.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer version;
    private LocalDate date;
    private String type;
    private String soort;
    private Boolean internal;
    private String status;
    private Integer level;
    
    private String safetyLevel;
    private String url;

    /**
     * This field is ignored during JSON serialization to prevent circular
     * references. This applies to all the relationships below.
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "documents")
    private List<Legislation> legislations;

    @JsonIgnore
    @ManyToMany(mappedBy = "documents")
    private List<ProcessEntity> processes;
}
