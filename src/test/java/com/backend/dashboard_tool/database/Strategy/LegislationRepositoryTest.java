package com.backend.dashboard_tool.database.Strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.Legislation;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LegislationRepositoryTest {
    @Autowired
    private LegislationRepository legislationRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding legislations by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        Legislation legislation = new Legislation();
        legislation.setProcesses(List.of(process));
        legislationRepository.save(legislation);

        process.setLegislations(List.of(legislation));
        processRepository.save(process);

        List<Legislation> foundLegislations = legislationRepository.findByProcessId(process.getId());
        assertThat(foundLegislations).isNotEmpty();
        assertThat(foundLegislations.get(0)).isEqualTo(legislation);
        assertThat(foundLegislations.size()).isEqualTo(1);
    }

    /**
     * Test finding legislations by process ID when no legislations are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Legislation> foundLegislations = legislationRepository.findByProcessId(process.getId());
        assertThat(foundLegislations).isEmpty();
    }
}
