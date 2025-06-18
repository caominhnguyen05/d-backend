package com.backend.dashboard_tool.database.Assets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Assets_Facilities.Location;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LocationRepositoryTest {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding locations by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        Location location = new Location();
        location.setProcesses(List.of(process));
        locationRepository.save(location);

        process.setLocations(List.of(location));
        processRepository.save(process);

        List<Location> foundLocations = locationRepository.findByProcessId(process.getId());
        assertThat(foundLocations).isNotEmpty();
        assertThat(foundLocations.get(0)).isEqualTo(location);
        assertThat(foundLocations.size()).isEqualTo(1);
    }

    /**
     * Test finding locations by process ID when no locations are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Location> foundLocations = locationRepository.findByProcessId(process.getId());
        assertThat(foundLocations).isEmpty();
    }
}
