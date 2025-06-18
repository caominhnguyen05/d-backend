package com.backend.dashboard_tool.entity.Process_Data;


import com.backend.dashboard_tool.entity.People.Organization;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("Trigger")
public class Trigger extends InformationFlow {
    /**
     * JsonIdentityReference is used to serialize the sourceOrganization.
     */
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "source_organization_id")
    private Organization sourceOrganization;

    /**
     * JsonIdentityReference is used to serialize the targetProcess.
     */
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "target_process_id")
    private ProcessEntity targetProcess;
}