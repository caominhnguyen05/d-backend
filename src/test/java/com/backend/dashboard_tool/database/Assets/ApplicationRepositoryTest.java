package com.backend.dashboard_tool.database.Assets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Assets_Facilities.Application;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ApplicationRepositoryTest {
    @Autowired
    private ApplicationRepository applicationRepository;

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

        Application application = new Application();
        application.setProcesses(List.of(process));
        applicationRepository.save(application);

        process.setApplications(List.of(application));
        processRepository.save(process);

        List<Application> foundApplications = applicationRepository.findByProcessId(process.getId());
        assertThat(foundApplications).isNotEmpty();
        assertThat(foundApplications.get(0)).isEqualTo(application);
        assertThat(foundApplications.size()).isEqualTo(1);
    }

    /**
     * Test finding applications by process ID when no applications are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Application> foundApplications = applicationRepository.findByProcessId(process.getId());
        assertThat(foundApplications).isEmpty();
    }
}
