package com.backend.dashboard_tool.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.backend.dashboard_tool.database.People.OrganizationRepository;
import com.backend.dashboard_tool.entity.People.Organization;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Process_Data.Trigger;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TriggerRepositoryTest {

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    /**
     * Test to verify that the findByTargetProcessId method retrieves all Trigger entities 
     */
    @Test
    void testFindByTargetProcessIdFound() {
        ProcessEntity process = new ProcessEntity();
        process.setType("Facilitating");
        process.setLevel(0);

        ProcessEntity process2 = new ProcessEntity();
        process2.setType("Primary");
        process2.setLevel(1);

        process = processRepository.save(process);
        process2 = processRepository.save(process2);

        Organization organization = new Organization();
        organization.setName("Test Organization");
        organization = organizationRepository.save(organization);

        Trigger trigger = new Trigger();
        trigger.setTargetProcess(process);
        trigger.setSourceOrganization(organization);
        triggerRepository.save(trigger);

        List<Trigger> result = triggerRepository.findByTargetProcessId(process.getId());
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(trigger);
    }

    /**
     * Test to verify that the findByTargetProcessId method returns an empty list when no triggers are found.
     */
    @Test
    void testFindByTargetProcessIdNotFound() {
        List<Trigger> result = triggerRepository.findByTargetProcessId(999L);
        assertThat(result).isEmpty();
    }
}
