package com.backend.dashboard_tool.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.DTO.ProcessDTO;

/**
 * Test class for ProcessService.
 * This class contains unit tests for the ProcessService methods.
 */
@ExtendWith(MockitoExtension.class)
public class ProcessServiceTest {
    @Mock 
    private ProcessRepository processRepository;

    @InjectMocks
    private ProcessService processService;

    /**
     * Helper method to create a ProcessEntity object.
     * 
     * @param id the ID of the process
     * @param type the type of the process
     * @param level the level of the process
     * @return a ProcessEntity object with the specified parameters
     */
    private ProcessEntity createProcessEntity(Long id, String type, int level) {
        ProcessEntity process = new ProcessEntity();
        process.setId(id);
        process.setType(type);
        process.setLevel(level);
        return process;
    }
    
    /**
     * Test for getSubProcesses() method in ProcessService.
     * This test will test the case when the parent id is provided and the level is 0.
     */
    @Test
    void getSubProcessesParentIdNotNullTypeFacilitating(){
        ProcessEntity process1 = createProcessEntity(1L, "Facilitating", 0);
        ProcessEntity process2 = createProcessEntity(2L, "Facilitating", 1);

        List<ProcessEntity> processes = Arrays.asList(process1, process2);

        //Mock the process repository to return the list of processes when findSubprocessByParentId is called
        when(processRepository.findSubprocessByParentId(1L)).thenReturn(processes);

        //Call the getSubProcesses method with parent id 1L and type "Facilitating"
        List<ProcessEntity> result = processService.getSubProcesses(1L, "Facilitating");

        //Assert that the result is not null and contains the expected processes
        assertEquals(2, result.size());
        assertEquals("Facilitating", result.get(0).getType());
        assertEquals(0, result.get(0).getLevel());
        assertEquals("Facilitating", result.get(1).getType());
        assertEquals(1, result.get(1).getLevel());
        //Verify that the process repository was called once with the correct parent id
        verify(processRepository, times(1)).findSubprocessByParentId(1L);
    }

    /**
     * Test for getSubProcesses() method in ProcessService.
     * This test will test the case when the parent id is not provided and the type is not provided.
     * @throws Exception
     */
    @Test
    void getSubProcessesParentIdNullTypeNull() throws Exception {
        //Assert throws a ResponseStatusException, because the type is null
        assertThrows(ResponseStatusException.class, 
        () -> processService.getSubProcesses(null, null));

        //Verify that the process repository was never called with any type.
        verify(processRepository, times(0)).findByType(anyString());
    }

    /**
     * Test for getSubProcesses() method in ProcessService.
     * This test will test the case when the parent id is not provided and the type is provided.
     */
    @Test
    void getSubProcessesParentIdNullTypeFacilitating() throws Exception {
        ProcessEntity process1 = createProcessEntity(1L, "Facilitating", 0);

        List<ProcessEntity> processes = Arrays.asList(process1);

        //Mock the process repository to return the list of processes when findSubprocessByParentId is called
        when(processRepository.findByTypeLevel0("Facilitating")).thenReturn(processes);

        //Call the getSubProcesses method with parent id null and type "Facilitating"
        List<ProcessEntity> result = processService.getSubProcesses(null, "Facilitating");

        //Assert that the result is not null and contains the expected processes
        assertEquals(1, result.size());
        assertEquals("Facilitating", result.get(0).getType());
        assertEquals(0, result.get(0).getLevel());
        //Verify that the process repository was called once with the correct parent id
        verify(processRepository, times(1)).findByTypeLevel0("Facilitating");
    }

    /**
     * Test for createProcess() method in ProcessService.
     * This test will verify the creation of a new process.
     */
    @Test
    void createProcessNoParentSuccess() {
        ProcessDTO processDTO = new ProcessDTO(
            "Test Process",    
            "Primary",        
            null,    
            null,          
            null, 
            null          
        );

        ProcessEntity processEntity = new ProcessEntity();
        processEntity.setName("Test Process");
        processEntity.setType("Primary");
        processEntity.setDescription(null);
        processEntity.setSoort(null);
        processEntity.setInternal(null);

        //Mock the save method of the repository
        when(processRepository.save(any(ProcessEntity.class))).thenReturn(processEntity);

        //Call the createProcess method
        ProcessEntity result = processService.createProcess(processDTO);

        //Assert that the result is not null and contains the expected values
        assertEquals("Test Process", result.getName());
        assertEquals("Primary", result.getType());

        //Verify that the save method was called once with the correct parameters
        verify(processRepository, times(1)).save(any(ProcessEntity.class));
    }

    /**
     * Test for createProcess() method in ProcessService.
     * This test will verify the creation of a new process with a parent process.
     */
    @Test
    void createProcessWithParentSuccess() {
        ProcessDTO processDTO = new ProcessDTO(
            "Test Process",
            "Primary",
            null,
            null,
            null,
            1L
        );

        ProcessEntity parentProcess = new ProcessEntity();
        parentProcess.setId(1L);
        parentProcess.setName("Parent Process");
        parentProcess.setType("Primary");
        parentProcess.setLevel(0);

        when(processRepository.findById(1L)).thenReturn(Optional.of(parentProcess));

        ProcessEntity processEntity = new ProcessEntity();
        processEntity.setName("Test Process");
        processEntity.setType("Primary");
        processEntity.setDescription(null);
        processEntity.setSoort(null);
        processEntity.setInternal(null);
        processEntity.setParentProcess(parentProcess);

        //Mock the save method of the repository
        when(processRepository.save(any(ProcessEntity.class))).thenReturn(processEntity);

        //Call the createProcess method
        ProcessEntity result = processService.createProcess(processDTO);

        //Assert that the result is not null and contains the expected values
        assertEquals("Test Process", result.getName());
        assertEquals("Primary", result.getType());
        assertEquals(1L, result.getParentProcess().getId());

        //Verify that the save method was called once with the correct parameters
        verify(processRepository, times(1)).save(any(ProcessEntity.class));
    }

    /**
     * Test for createProcess() method in ProcessService.
     * This test will verify the creation of a new process with an invalid parent process.
     */
    @Test
    void createProcessWithInvalidParent() {
        ProcessDTO processDTO = new ProcessDTO(
            "Test Process",
            "Primary",
            null,
            null,
            null,
            999L // Non-existing parent ID
        );

        //Mock the repository to return an empty Optional when trying to find the parent process
        when(processRepository.findById(999L)).thenReturn(Optional.empty());

        //Assert that a ResponseStatusException is thrown when trying to create a process with a non-existing parent
        assertThrows(ResponseStatusException.class, () -> processService.createProcess(processDTO));

        //Verify that the save method was never called
        verify(processRepository, never()).save(any(ProcessEntity.class));
    }

    /**
     * Test for createProcess() method in ProcessService.  
     * This test will verify the creation of a new process with a parent process of a different type.
     */
    @Test
    void createProcessWithParentDifferentType() {
        ProcessDTO processDTO = new ProcessDTO(
            "Test Process",
            "Primary",
            null,
            null,
            null,
            1L // Parent ID
        );

        ProcessEntity parentProcess = new ProcessEntity();
        parentProcess.setId(1L);
        parentProcess.setName("Parent Process");
        parentProcess.setType("Facilitating"); // Different type than the new process
        parentProcess.setLevel(0);

        when(processRepository.findById(1L)).thenReturn(Optional.of(parentProcess));

        //Assert that a ResponseStatusException is thrown when trying to create a process with a parent of a different type
        assertThrows(ResponseStatusException.class, () -> processService.createProcess(processDTO));

        //Verify that the save method was never called
        verify(processRepository, never()).save(any(ProcessEntity.class));
    }

    /**
     * Test for getProcessById() method in ProcessService.
     * This test will verify the retrieval of a process by its ID.
     */
    @Test
    void getProcessByIdSuccess() {
        ProcessEntity processEntity = createProcessEntity(1L, "Primary", 0);

        //Mock the repository to return the process when findById is called
        when(processRepository.findById(1L)).thenReturn(Optional.of(processEntity));

        //Call the getProcessById method
        ProcessEntity result = processService.getProcessById(1L);

        //Assert that the result is not null and contains the expected values
        assertEquals("Primary", result.getType());
        assertEquals(0, result.getLevel());

        //Verify that the findById method was called once with the correct ID
        verify(processRepository, times(1)).findById(1L);
    }

    /**
     * Test for getProcessById() method in ProcessService.
     * This test will verify the case when the process with the given ID does not exist.
     */
    @Test
    void getProcessByIdNotFound() {
        //Mock the repository to return an empty Optional when trying to find a process by ID
        when(processRepository.findById(999L)).thenReturn(Optional.empty());
        
        //Assert that a ResponseStatusException is thrown when trying to retrieve a process with a non-existing ID
        assertThrows(ResponseStatusException.class, () -> processService.getProcessById(999L));

        //Verify that the findById method was called once with the correct ID
        verify(processRepository, times(1)).findById(999L);
    }
}
