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
@DiscriminatorValue("Result")
public class Result extends InformationFlow {
    /**
     * jsonIdentityReference is used to serialize the sourceProcess.
     */
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "source_process_id")
    private ProcessEntity sourceProcess;

    /**
     * jsonIdentityReference is used to serialize the targetOrganization.
     */
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "target_organization_id")
    private Organization targetOrganization;
}
