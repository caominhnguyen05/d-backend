package com.backend.dashboard_tool.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.DTO.ResultDTO;
import com.backend.dashboard_tool.database.ResultRepository;
import com.backend.dashboard_tool.entity.People.Organization;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Process_Data.Result;
import com.backend.dashboard_tool.database.People.OrganizationRepository;
import com.backend.dashboard_tool.sipocrecords.ResultOrgPair;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ResultServiceTest {
    @Mock
    private ResultRepository resultRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private ProcessService processService;
    @InjectMocks
    private ResultService resultService;
  
    /**
     * Creates a mock Organization object with the given ID and name.
     * @param id
     * @param name
     * @return mock Organization object
     */
    private Organization createOrganization(Long id, String name) {
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(name);
        return organization;
    }

    /**
     * Creates a mock ProcessEntity object with the given ID and name.
     * @param id
     * @param name
     * @return mock ProcessEntity object
     */
    private ProcessEntity createProcess(Long id, String name) {
        ProcessEntity process = new ProcessEntity();
        process.setId(id);
        process.setName(name);
        return process;
    }

    /**
     * Creates a mock Trigger object with the given ID, name, organization, and process.
     * @param id
     * @param name
     * @param org
     * @param process
     * @return mock Trigger object
     */
    private Result createResult(Long id, String name, Organization org, ProcessEntity process) {
        Result result = new Result();
        result.setId(id);
        result.setName(name);
        result.setSourceProcess(process);
        result.setTargetOrganization(org);
        return result;
    }

    /**
     * Test case for successfully retrieving results by process ID.
     */
    @Test
    void getResultsByProcessIdSuccess(){
        Long processId = 1L;
        Result result = createResult(1L, "Test Result", createOrganization(1L, "Test Org"), createProcess(processId, "Test Process"));
        when(resultRepository.findBySourceProcessId(processId)).thenReturn(List.of(result));

        Iterable<ResultOrgPair> results = resultService.getResultsByProcessId(processId);

        assertNotNull(results);
        assertEquals(1, ((Collection<?>) results).size());
    }

    /**
     * Test case for retrieving results by process ID when no results are found.
     */
    @Test
    void getResultsByProcessIdEmpty() {
        Long processId = 1L;
        when(resultRepository.findBySourceProcessId(processId)).thenReturn(Collections.emptyList());

        Iterable<ResultOrgPair> results = resultService.getResultsByProcessId(processId);

        assertNotNull(results);
        assertEquals(0, ((Collection<?>) results).size());
    }   

    /**
     * Test case for creating a result successfully
     */
    @Test
    void createResultSucess(){
        ResultDTO resultDTO = new ResultDTO(
            "Test Result", 
            "Type A", 
            true, 
            null, 
            1L, 
            1L
        );
        Organization sourceOrganization = createOrganization(1L, "Test Org");
        ProcessEntity targetProcess = createProcess(1L, "Test Process");
        Result result = createResult(1L, "Test Result", sourceOrganization, targetProcess);

        when(organizationRepository.getReferenceById(resultDTO.source())).thenReturn(sourceOrganization);
        when(processService.getProcessById(resultDTO.target())).thenReturn(targetProcess);
        when(resultRepository.save(any(Result.class))).thenReturn(result);

        Result createdResult = resultService.createResult(resultDTO);

        assertNotNull(createdResult);
        assertEquals(result.getId(), createdResult.getId());
        assertEquals(result.getName(), createdResult.getName());
        verify(organizationRepository, times(1)).getReferenceById(resultDTO.source());
        verify(processService, times(1)).getProcessById(resultDTO.target());
        verify(resultRepository, times(1)).save(any(Result.class));
    }

    /**
     * Test case for creating a result when the source organization is not found.
     */
    @Test
    void createResultSourceOrganizationNotFound() {
        ResultDTO resultDTO = new ResultDTO(
            "Test Result", 
            "Type A", 
            true, 
            null, 
            1L, 
            1L
        );

        when(organizationRepository.getReferenceById(resultDTO.source())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            resultService.createResult(resultDTO);
        });
    }

    /**
     * Test case for creating a result when the target process is not found.
     */
    @Test
    void createResultTargetProcessNotFound() {
        ResultDTO resultDTO = new ResultDTO(
            "Test Result", 
            "Type A", 
            true, 
            null, 
            1L, 
            1L
        );
        Organization sourceOrganization = createOrganization(1L, "Test Org");

        when(organizationRepository.getReferenceById(resultDTO.source())).thenReturn(sourceOrganization);
        when(processService.getProcessById(resultDTO.target())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            resultService.createResult(resultDTO);
        });
    }
}
