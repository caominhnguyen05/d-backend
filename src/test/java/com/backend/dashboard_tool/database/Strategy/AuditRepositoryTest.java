package com.backend.dashboard_tool.database.Strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.Audit;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuditRepositoryTest {
    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding applications by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        Audit audit = new Audit();
        audit.setProcesses(List.of(process));
        auditRepository.save(audit);

        process.setAudits(List.of(audit));
        processRepository.save(process);

        List<Audit> foundAudits = auditRepository.findByProcessId(process.getId());
        assertThat(foundAudits).isNotEmpty();
        assertThat(foundAudits.get(0)).isEqualTo(audit);
        assertThat(foundAudits.size()).isEqualTo(1);
    }

    /**
     * Test finding audits by process ID when no audits are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Audit> foundAudits = auditRepository.findByProcessId(process.getId());
        assertThat(foundAudits).isEmpty();
    }
}
