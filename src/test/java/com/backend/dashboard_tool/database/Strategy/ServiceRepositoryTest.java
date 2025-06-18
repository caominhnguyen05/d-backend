package com.backend.dashboard_tool.database.Strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.Service;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ServiceRepositoryTest {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding services by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        Service service = new Service();
        service.setProcesses(List.of(process));
        serviceRepository.save(service);

        process.setServices(List.of(service));
        processRepository.save(process);

        List<Service> foundServices = serviceRepository.findByProcessId(process.getId());
        assertThat(foundServices).isNotEmpty();
        assertThat(foundServices.get(0)).isEqualTo(service);
        assertThat(foundServices.size()).isEqualTo(1);
    }

    /**
     * Test finding services by process ID when no services are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Service> foundServices = serviceRepository.findByProcessId(process.getId());
        assertThat(foundServices).isEmpty();
    }
}
