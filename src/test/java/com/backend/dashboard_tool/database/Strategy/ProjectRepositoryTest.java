package com.backend.dashboard_tool.database.Strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.Project;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding projects by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        Project project = new Project();
        project.setProcesses(List.of(process));
        projectRepository.save(project);

        process.setProjects(List.of(project));
        processRepository.save(process);

        List<Project> foundProjects = projectRepository.findByProcessId(process.getId());
        assertThat(foundProjects).isNotEmpty();
        assertThat(foundProjects.get(0)).isEqualTo(project);
        assertThat(foundProjects.size()).isEqualTo(1);
    }

    /**
     * Test finding projects by process ID when no projects are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Project> foundProjects = projectRepository.findByProcessId(process.getId());
        assertThat(foundProjects).isEmpty();
    }
}
