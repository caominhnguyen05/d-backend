package com.backend.dashboard_tool.database.Strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.Program;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProgramRepositoryTest {
    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding programs by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        Program program = new Program();
        program.setProcesses(List.of(process));
        programRepository.save(program);

        process.setPrograms(List.of(program));
        processRepository.save(process);

        List<Program> foundPrograms = programRepository.findByProcessId(process.getId());
        assertThat(foundPrograms).isNotEmpty();
        assertThat(foundPrograms.get(0)).isEqualTo(program);
        assertThat(foundPrograms.size()).isEqualTo(1);
    }

    /**
     * Test finding programs by process ID when no programs are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Program> foundPrograms = programRepository.findByProcessId(process.getId());
        assertThat(foundPrograms).isEmpty();
    }
}
