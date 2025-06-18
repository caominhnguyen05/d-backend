package com.backend.dashboard_tool.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.Strategy.ImprovementRepository;
import com.backend.dashboard_tool.database.Strategy.ProjectRepository;
import com.backend.dashboard_tool.entity.Process_Data.Improvement;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.Project;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
public class InitiativeServiceTest {
    @Mock
    private ImprovementRepository improvementRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProcessService processService;

    @InjectMocks
    private InitiativeService initiativeService;


    /**
     * Creates a mock Improvement object for testing purposes.
     * @param id
     * @param name
     * @return Improvement object with specified id and name
     */
    private Improvement createImprovement(Long id, String name) {
        Improvement improvement = new Improvement();
        improvement.setId(id);
        improvement.setName(name);
        return improvement;
    }

    /**
     * Creates a mock Project object for testing purposes.
     * @param id
     * @param name
     * @return Project object with specified id and name
     */
    private Project createProject(Long id, String name) {
        Project project = new Project();
        project.setId(id);
        project.setName(name);
        return project;
    }

    // --- /all endpoint tests ---

    @Test
    void getAllImprovements() throws Exception {
        List<Improvement> improvements = List.of(createImprovement(1L, "Improvement 1"));
        when(improvementRepository.findAll()).thenReturn(improvements);

        List<?> result = initiativeService.getAllInitiativesByType("improvement");
        assertEquals(improvements, result);
        verify(improvementRepository, times(1)).findAll();
    }

    @Test
    void getAllProjects() throws Exception {
        List<Project> projects = List.of(createProject(1L, "Project 1"));
        when(projectRepository.findAll()).thenReturn(projects);

        List<?> result = initiativeService.getAllInitiativesByType("project");
        assertEquals(projects, result);
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void getAllInitiativesByType_UnknownType() throws Exception {
        assertThrows(ResponseStatusException.class, () -> {
            initiativeService.getAllInitiativesByType("unknown");
        });
    }

    // --- /process endpoint tests ---

    @Test
    void getImprovementsByProcessId() throws Exception {
        Long processId = 10L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Improvement> improvements = List.of(createImprovement(1L, "Improvement 1"));
        when(improvementRepository.findByProcessId(processId)).thenReturn(improvements);

        List<?> result = initiativeService.getInitiativesByTypeAndProcessId("improvement", processId);
        assertEquals(improvements, result);
        verify(improvementRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getProjectsByProcessId() throws Exception {
        Long processId = 11L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Project> projects = List.of(createProject(1L, "Project 1"));
        when(projectRepository.findByProcessId(processId)).thenReturn(projects);

        List<?> result = initiativeService.getInitiativesByTypeAndProcessId("project", processId);
        assertEquals(projects, result);
        verify(projectRepository, times(1)).findByProcessId(processId);}

    @Test
    void getInitiativesByTypeAndProcessId_UnknownProcessId() throws Exception {
        Long processId = 99L;
        when(processService.getProcessById(processId)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            initiativeService.getInitiativesByTypeAndProcessId("improvement", processId);
        });
    }

    @Test
    void getInitiativesByTypeAndProcessId_UnknownType() throws Exception {
        Long processId = 99L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        assertThrows(ResponseStatusException.class, () -> {
            initiativeService.getInitiativesByTypeAndProcessId("unknown", processId);
        });
    }
}
