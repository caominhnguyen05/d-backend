package com.backend.dashboard_tool.database.Strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.Risk;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RiskRepositoryTest {
    @Autowired
    private RiskRepository riskRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding risks by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        Risk risk = new Risk();
        risk.setProcesses(List.of(process));
        riskRepository.save(risk);

        process.setRisks(List.of(risk));
        processRepository.save(process);

        List<Risk> foundRisks = riskRepository.findByProcessId(process.getId());
        assertThat(foundRisks).isNotEmpty();
        assertThat(foundRisks.get(0)).isEqualTo(risk);
        assertThat(foundRisks.size()).isEqualTo(1);
    }

    /**
     * Test finding risks by process ID when no risks are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Risk> foundRisks = riskRepository.findByProcessId(process.getId());
        assertThat(foundRisks).isEmpty();
    }
}
