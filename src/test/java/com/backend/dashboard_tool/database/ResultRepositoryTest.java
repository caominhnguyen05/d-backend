package com.backend.dashboard_tool.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.backend.dashboard_tool.database.People.OrganizationRepository;
import com.backend.dashboard_tool.entity.People.Organization;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Process_Data.Result;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ResultRepositoryTest {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    /**
     * Test to verify that the findBySourceProcessId method retrieves all Trigger entities 
     */
    @Test
    void testFindBySourceProcessIdFound() {
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

        Result result = new Result();
        result.setSourceProcess(process);
        result.setTargetOrganization(organization);
        resultRepository.save(result);

        List<Result> results = resultRepository.findBySourceProcessId(process.getId());
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isEqualTo(result);
    }

    /**
     * Test to verify that the findByTargetProcessId method returns an empty list when no triggers are found.
     */
    @Test
    void testFindBySourceProcessIdNotFound() {
        List<Result> result = resultRepository.findBySourceProcessId(999L);
        assertThat(result).isEmpty();
    }
}
