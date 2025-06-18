package com.backend.dashboard_tool.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.entity.Process_Data.Dataflow;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DataflowRepositoryTest {

    @Autowired
    private DataflowRepository dataflowRepository;

    @Autowired
    private ProcessRepository processRepository;
    
    private ProcessEntity createProcessEntity(String type, int level) {
        ProcessEntity process = new ProcessEntity();
        process.setType(type);
        process.setLevel(level);
        return process;
    }  

    private Dataflow createDataflow(ProcessEntity sourceProcess, ProcessEntity targetProcess) {
        Dataflow dataflow = new Dataflow();
        dataflow.setSourceProcess(sourceProcess);
        dataflow.setTargetProcess(targetProcess);
        return dataflow;
    }

    /**
     * Test finding a dataflow by its target process ID when it exists.
     */
    @Test
    void findByTargetProcessIdFound() {
        ProcessEntity process1 = createProcessEntity("Primary", 0);
        ProcessEntity process2 = createProcessEntity("Facilitating", 1);
        Dataflow dataflow = createDataflow(process1, process2);
        Dataflow dataflow2 = createDataflow(process2, process1);

        processRepository.save(process1);
        processRepository.save(process2);
        dataflowRepository.save(dataflow);
        dataflowRepository.save(dataflow2);
        List<Dataflow> foundDataflows = dataflowRepository.findByTargetProcessId(process2.getId());

        assertThat(foundDataflows).isNotEmpty();
        assertThat(foundDataflows.get(0)).isEqualTo(dataflow);
        assertThat(foundDataflows.size()).isEqualTo(1);
    }

    /**
     * Test finding a dataflow by its target process ID when it does not exist.
     */
    @Test
    void findByTargetProcessIdEmpty() {
        ProcessEntity process1 = createProcessEntity("Primary", 0);
        ProcessEntity process2 = createProcessEntity("Facilitating", 1);
        Dataflow dataflow = createDataflow(process1, process2);
        Dataflow dataflow2 = createDataflow(process2, process1);

        processRepository.save(process1);
        processRepository.save(process2);
        dataflowRepository.save(dataflow);
        dataflowRepository.save(dataflow2);
        List<Dataflow> foundDataflows = dataflowRepository.findByTargetProcessId(999L);

        assertThat(foundDataflows).isEmpty();
    }

    /**
     * Test finding a dataflow by its source process ID when it exists.
     */
    @Test
    void findBySourceProcessIdFound() {
        ProcessEntity process1 = createProcessEntity("Primary", 0);
        ProcessEntity process2 = createProcessEntity("Facilitating", 1);
        Dataflow dataflow = createDataflow(process1, process2);
        Dataflow dataflow2 = createDataflow(process2, process1);

        processRepository.save(process1);
        processRepository.save(process2);
        dataflowRepository.save(dataflow);
        dataflowRepository.save(dataflow2);
        List<Dataflow> foundDataflows = dataflowRepository.findBySourceProcessId(process1.getId());

        assertThat(foundDataflows).isNotEmpty();
        assertThat(foundDataflows.get(0)).isEqualTo(dataflow);
        assertThat(foundDataflows.size()).isEqualTo(1);
    }

    /**
     * Test finding a dataflow by its source process ID when it does not exist.
     */
    @Test
    void findBySourceProcessIdEmpty() {
        ProcessEntity process1 = createProcessEntity("Primary", 0);
        ProcessEntity process2 = createProcessEntity("Facilitating", 1);
        Dataflow dataflow = createDataflow(process1, process2);
        Dataflow dataflow2 = createDataflow(process2, process1);

        processRepository.save(process1);
        processRepository.save(process2);
        dataflowRepository.save(dataflow);
        dataflowRepository.save(dataflow2);
        List<Dataflow> foundDataflows = dataflowRepository.findBySourceProcessId(999L);

        assertThat(foundDataflows).isEmpty();
    }

    /**
     * Test finding dataflows by a list of process IDs when they exist.
     */
    @Test
    void findByProcessIdsFound(){
        ProcessEntity process1 = createProcessEntity("Primary", 0);
        ProcessEntity process2 = createProcessEntity("Facilitating", 1);
        Dataflow dataflow = createDataflow(process1, process2);
        Dataflow dataflow2 = createDataflow(process2, process1);

        processRepository.save(process1);
        processRepository.save(process2);
        dataflowRepository.save(dataflow);
        dataflowRepository.save(dataflow2);
        List<Dataflow> foundDataflows = dataflowRepository.findByProcessIds(List.of(process1.getId(), process2.getId()));

        assertThat(foundDataflows).isNotEmpty();
        assertThat(foundDataflows.get(0)).isEqualTo(dataflow);
        assertThat(foundDataflows.get(1)).isEqualTo(dataflow2);
        assertThat(foundDataflows.size()).isEqualTo(2);
    }

    /**
     * Test finding dataflows by a list of process IDs when they do not exist.
     */
    @Test
    void findByProcessIdsEmpty(){
        List<Dataflow> foundDataflows = dataflowRepository.findByProcessIds(List.of(999L, 888L));

        assertThat(foundDataflows).isEmpty();
    }

    /**
     * Test finding dataflows by a list of process IDs when some exist and some do not.
     */
    @Test
    void findInputDataflowsFound() {
        ProcessEntity process1 = createProcessEntity("Primary", 0);
        ProcessEntity process2 = createProcessEntity("Facilitating", 1);
        ProcessEntity process3 = createProcessEntity("Steering", 0);
        Dataflow dataflow = createDataflow(process1, process2);
        Dataflow dataflow2 = createDataflow(process2, process1);
        Dataflow dataflow3 = createDataflow(process3, process1);

        processRepository.save(process1);
        processRepository.save(process2);
        processRepository.save(process3);
        dataflowRepository.save(dataflow);
        dataflowRepository.save(dataflow2);
        dataflowRepository.save(dataflow3);

        List<Dataflow> foundDataflows = dataflowRepository.findInputDataflows(process1.getId(), null,"Primary");

        assertThat(foundDataflows).isNotEmpty();
        assertThat(foundDataflows.get(0)).isEqualTo(dataflow2);
        assertThat(foundDataflows.get(1)).isEqualTo(dataflow3);
        assertThat(foundDataflows.size()).isEqualTo(2);
    }

    /**
     * Test finding dataflows by a list of process IDs when they do not exist.
     */
    @Test
    void findInputDataflowsEmpty() {
        ProcessEntity process1 = createProcessEntity("Primary", 0);
        ProcessEntity process2 = createProcessEntity("Facilitating", 1);
        Dataflow dataflow = createDataflow(process1, process2);
        Dataflow dataflow2 = createDataflow(process2, process1);

        processRepository.save(process1);
        processRepository.save(process2);
        dataflowRepository.save(dataflow);
        dataflowRepository.save(dataflow2);

        List<Dataflow> foundDataflows = dataflowRepository.findInputDataflows(999L, null,"Primary");

        assertThat(foundDataflows).isEmpty();
    }

    /**
     * Test finding output dataflows by a list of process IDs when they exist.
     */
    @Test
    void findOutputDataflowsFound() {
       ProcessEntity process1 = createProcessEntity("Primary", 0);
        ProcessEntity process2 = createProcessEntity("Facilitating", 1);
        ProcessEntity process3 = createProcessEntity("Steering", 0);
        Dataflow dataflow = createDataflow(process1, process2);
        Dataflow dataflow2 = createDataflow(process2, process1);
        Dataflow dataflow3 = createDataflow(process3, process1);

        processRepository.save(process1);
        processRepository.save(process2);
        processRepository.save(process3);
        dataflowRepository.save(dataflow);
        dataflowRepository.save(dataflow2);
        dataflowRepository.save(dataflow3);

        List<Dataflow> foundDataflows = dataflowRepository.findOutputDataflows(process1.getId(), null, "Primary");

        assertThat(foundDataflows).isNotEmpty();
        assertThat(foundDataflows.get(0)).isEqualTo(dataflow);
        assertThat(foundDataflows.size()).isEqualTo(1);
    }

    /**
     * Test finding output dataflows by a list of process IDs when they do not exist.
     */
    @Test
    void findOutputDataflowsEmpty() {
        ProcessEntity process1 = createProcessEntity("Primary", 0);
        ProcessEntity process2 = createProcessEntity("Facilitating", 1);
        Dataflow dataflow = createDataflow(process1, process2);
        Dataflow dataflow2 = createDataflow(process2, process1);

        processRepository.save(process1);
        processRepository.save(process2);
        dataflowRepository.save(dataflow);
        dataflowRepository.save(dataflow2);

        List<Dataflow> foundDataflows = dataflowRepository.findOutputDataflows(999L, null,"Primary");

        assertThat(foundDataflows).isEmpty();
    }

    /**
     * Test finding input dataflows by a list of process IDs when parent Id is provided.
     */
    @Test
    void findInputDataflowsWithParentId() {
        ProcessEntity process1 = createProcessEntity("Primary", 1);
        ProcessEntity process2 = createProcessEntity("Primary", 1);
        ProcessEntity process3 = createProcessEntity("Primary", 0);
        ProcessEntity process4 = createProcessEntity("Primary", 0);

        process1.setParentProcess(process3);
        process2.setParentProcess(process4);
        Dataflow dataflow = createDataflow(process2, process1);
        Dataflow dataflow2 = createDataflow(process4, process1);
        Dataflow dataflow3 = createDataflow(process3, process1);

        processRepository.save(process1);
        processRepository.save(process2);
        processRepository.save(process3);
        processRepository.save(process4);
        dataflowRepository.save(dataflow);
        dataflowRepository.save(dataflow2);
        dataflowRepository.save(dataflow3);

        List<Dataflow> foundDataflows = dataflowRepository.findInputDataflows(process1.getId(), process3.getId(), "Primary");

        assertThat(foundDataflows).isNotEmpty();
        assertThat(foundDataflows.get(0)).isEqualTo(dataflow);
        assertThat(foundDataflows.get(1)).isEqualTo(dataflow2);
        assertThat(foundDataflows.get(2)).isEqualTo(dataflow3);
        assertThat(foundDataflows.size()).isEqualTo(3);
    }

    /**
     * Test finding output dataflows by a list of process IDs when parent Id is provided.
     */
    @Test
    void findOutputDataflowsWithParentId() {
        ProcessEntity process1 = createProcessEntity("Primary", 1);
        ProcessEntity process2 = createProcessEntity("Primary", 1);
        ProcessEntity process3 = createProcessEntity("Primary", 0);
        ProcessEntity process4 = createProcessEntity("Primary", 0);

        process1.setParentProcess(process3);
        process2.setParentProcess(process4);
        Dataflow dataflow = createDataflow(process1, process2);
        Dataflow dataflow2 = createDataflow(process1, process4);
        Dataflow dataflow3 = createDataflow(process1, process3);

        processRepository.save(process1);
        processRepository.save(process2);
        processRepository.save(process3);
        processRepository.save(process4);
        dataflowRepository.save(dataflow);
        dataflowRepository.save(dataflow2);
        dataflowRepository.save(dataflow3);

        List<Dataflow> foundDataflows = dataflowRepository.findOutputDataflows(process1.getId(), process3.getId(), "Primary");

        assertThat(foundDataflows).isNotEmpty();
        assertThat(foundDataflows.get(0)).isEqualTo(dataflow);
        assertThat(foundDataflows.get(1)).isEqualTo(dataflow2);
        assertThat(foundDataflows.get(2)).isEqualTo(dataflow3);
        assertThat(foundDataflows.size()).isEqualTo(3);
    }
}
