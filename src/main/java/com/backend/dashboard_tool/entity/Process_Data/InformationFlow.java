package com.backend.dashboard_tool.entity.Process_Data;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DiscriminatorFormula;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(
    name = "information_flow",
    indexes = {
        @Index(name = "idx_source_process", columnList = "source_process_id"),
        @Index(name = "idx_target_process", columnList = "target_process_id"),
        @Index(name = "idx_source_organization", columnList = "source_organization_id"),
        @Index(name = "idx_target_organization", columnList = "target_organization_id")
    }
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("case " + 
    "when source_process_id is not null and target_process_id is not null and source_organization_id is null and target_organization_id is null then 'Dataflow' " +
    "when source_process_id is null and target_process_id is not null and source_organization_id is not null and target_organization_id is null then 'Trigger' " +
    "when source_process_id is not null and target_process_id is null and source_organization_id is null and target_organization_id is not null then 'Result' " +
    "else 'Unknown' end") //This discriminator formula is used to determine the type of information flow
public abstract class InformationFlow {
    /**
     * The unique identifier for the information flow.
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
    @OneToMany(mappedBy = "informationFlow")
    private List<DataAttribute> dataAttributes = new ArrayList<>();
}
