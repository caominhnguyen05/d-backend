package com.backend.dashboard_tool.entity.Process_Data;


import com.fasterxml.jackson.annotation.JsonIdentityReference;

import jakarta.persistence.*;
import lombok.*;

/**
 * This class is used to represent the dataflow between two processes.
 * It contains fields for the source and target processes.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("Dataflow")
public class Dataflow extends InformationFlow {
    /**
     * JsonIdentityReference is used to serialize the sourceProcess.
     */
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "source_process_id")
    private ProcessEntity sourceProcess;

    /**
     * JsonIdentityReference is used to serialize the targetProcess.
     */
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "target_process_id")
    private ProcessEntity targetProcess;
}
