package com.backend.dashboard_tool.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProcessRepositoryTest {
    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test to verify that the findById method retrieves a ProcessEntity by its ID.
     */
    @Test
    void testFindByIdSuccess(){
        // Create and save a ProcessEntity instance
        ProcessEntity process = new ProcessEntity();
        process.setType("TestProcess");
        process.setLevel(0);
        ProcessEntity savedProcess = processRepository.save(process);

        // Retrieve the ProcessEntity by its ID
        ProcessEntity foundProcess = processRepository.findById(savedProcess.getId()).orElse(null);

        // Assertions to verify the retrieved entity
        assertThat(foundProcess).isNotNull();
        assertThat(foundProcess.getId()).isEqualTo(savedProcess.getId());
        assertThat(foundProcess.getType()).isEqualTo("TestProcess");
    }

    /**
     * Test to verify that the findById method returns an empty Optional when the ID does not exist.
     */
    @Test
    void testFindByIdNotFound(){
        // Attempt to retrieve a ProcessEntity by a non-existent ID
        Long id = 999L;
        ProcessEntity foundProcess = processRepository.findById(id).orElse(null);
        // Assertions to verify that no entity is found
        assertThat(foundProcess).isNull();
    }

    /**
     * Test to verify that the findByType method retrieves all ProcessEntity instances with the specified type.
     */
    @Test
    void testFindByType() {
        // Create and save some ProcessEntity instances with different types
        ProcessEntity p1 = new ProcessEntity();
        p1.setType("Primary");
        processRepository.save(p1);

        ProcessEntity p2 = new ProcessEntity();
        p2.setType("Facilitating");
        processRepository.save(p2);

        ProcessEntity p3 = new ProcessEntity();
        p3.setType("Primary");
        processRepository.save(p3);

        List<ProcessEntity> result = processRepository.findByType("Primary");

        //Assertions with correct results
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getType()).isEqualTo("Primary");
        assertThat(result.get(1).getType()).isEqualTo("Primary");
    }


    /**
     * Test to verify that the findByTypeLevel0 method retrieves all ProcessEntity instances with the specified type.
     */
    @Test
    void testFindByTypeLevel0() {
        // Create and save some ProcessEntity instances with different types and level
        ProcessEntity p1 = new ProcessEntity();
        p1.setType("Primary");
        p1.setLevel(0);
        processRepository.save(p1);

        ProcessEntity p2 = new ProcessEntity();
        p2.setType("Primary");
        p2.setLevel(1);
        processRepository.save(p2);

        ProcessEntity p3 = new ProcessEntity();
        p3.setType("Facilitating");
        p3.setLevel(0);
        processRepository.save(p3);

        List<ProcessEntity> result = processRepository.findByTypeLevel0("Primary");

        //Assertions with correct results
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo("Primary");
        assertThat(result.get(0).getLevel()).isEqualTo(0);
    }

    /**
     * Test to verify that the findByTypeLevel0 method retrieves all ProcessEntity instances with the specified type.
     */
    @Test
    void testFindByTypeLevel0Empty() {
        // Create and save some ProcessEntity instances with different types and level
        ProcessEntity p1 = new ProcessEntity();
        p1.setType("Primary");
        p1.setLevel(1);
        processRepository.save(p1);


        List<ProcessEntity> result = processRepository.findByTypeLevel0("Primary");

        //Assertions with correct results
        assertThat(result).isEmpty();
    }

    /**
     * Test to verify that the findSubprocessByParentId method retrieves all subprocesses of a given parent process.
     */
    @Test
    void testFindSubprocessByParentIdSuccess() {
        // Create and save a parent ProcessEntity
        ProcessEntity parent = new ProcessEntity();
        parent.setType("ParentProcess");
        processRepository.save(parent);

        // Create and save some subprocesses
        ProcessEntity child1 = new ProcessEntity();
        child1.setType("Subprocess");
        child1.setParentProcess(parent);
        processRepository.save(child1);

        ProcessEntity child2 = new ProcessEntity();
        child2.setType("Subprocess");
        child2.setParentProcess(parent);
        processRepository.save(child2);

        // Retrieve subprocesses by parent ID
        List<ProcessEntity> result = processRepository.findSubprocessByParentId(parent.getId());

        // Assertions to verify the retrieved subprocesses
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getParentProcess()).isEqualTo(parent);
        assertThat(result.get(1).getParentProcess()).isEqualTo(parent);
    }

    /**
     * Test to verify that the findSubprocessByParentId method returns an empty list when there are no subprocesses.
     */
    @Test
    void testFindSubprocessByParentIdNotFound() {
        // Create and save a parent ProcessEntity
        ProcessEntity parent = new ProcessEntity();
        parent.setType("ParentProcess");
        processRepository.save(parent);

        // Attempt to retrieve subprocesses by non-existent parent ID
        List<ProcessEntity> result = processRepository.findSubprocessByParentId(999L);

        // Assertions to verify that no subprocesses are found
        assertThat(result).isEmpty();
    }
}
