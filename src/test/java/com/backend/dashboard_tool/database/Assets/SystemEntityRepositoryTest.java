package com.backend.dashboard_tool.database.Assets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Assets_Facilities.SystemEntity;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SystemEntityRepositoryTest {
    @Autowired
    private SystemEntityRepository systemEntityRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding system entities by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        SystemEntity systemEntity = new SystemEntity();
        systemEntity.setProcesses(List.of(process));
        systemEntityRepository.save(systemEntity);

        process.setSystems(List.of(systemEntity));
        processRepository.save(process);

        List<SystemEntity> foundSystemEntities = systemEntityRepository.findByProcessId(process.getId());
        assertThat(foundSystemEntities).isNotEmpty();
        assertThat(foundSystemEntities.get(0)).isEqualTo(systemEntity);
        assertThat(foundSystemEntities.size()).isEqualTo(1);
    }

    /**
     * Test finding system entities by process ID when no system entities are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<SystemEntity> foundSystemEntities = systemEntityRepository.findByProcessId(process.getId());
        assertThat(foundSystemEntities).isEmpty();
    }
}
