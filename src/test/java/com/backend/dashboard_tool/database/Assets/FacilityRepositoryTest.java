package com.backend.dashboard_tool.database.Assets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Assets_Facilities.Facility;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FacilityRepositoryTest {
    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding facilities by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        Facility facility = new Facility();
        facility.setProcesses(List.of(process));
        facilityRepository.save(facility);

        process.setFacilities(List.of(facility));
        processRepository.save(process);

        List<Facility> foundFacilities = facilityRepository.findByProcessId(process.getId());
        assertThat(foundFacilities).isNotEmpty();
        assertThat(foundFacilities.get(0)).isEqualTo(facility);
        assertThat(foundFacilities.size()).isEqualTo(1);
    }

    /**
     * Test finding facilities by process ID when no facilities are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Facility> foundFacilities = facilityRepository.findByProcessId(process.getId());
        assertThat(foundFacilities).isEmpty();
    }
}
