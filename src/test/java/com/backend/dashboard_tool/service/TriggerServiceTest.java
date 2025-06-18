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

import com.backend.dashboard_tool.DTO.TriggerDTO;
import com.backend.dashboard_tool.database.TriggerRepository;
import com.backend.dashboard_tool.entity.People.Organization;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Process_Data.Trigger;
import com.backend.dashboard_tool.database.People.OrganizationRepository;
import com.backend.dashboard_tool.sipocrecords.OrgTriggerPair;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class TriggerServiceTest {
    @Mock
    private TriggerRepository triggerRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private ProcessService processService;
    @InjectMocks
    private TriggerService triggerService;
    
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
    private Trigger createTrigger(Long id, String name, Organization org, ProcessEntity process) {
        Trigger trigger = new Trigger();
        trigger.setId(id);
        trigger.setName(name);
        trigger.setSourceOrganization(org);
        trigger.setTargetProcess(process);
        return trigger;
    }

    /**
     * Test case for successfully retrieving triggers by process ID.
     */
    @Test
    void getTriggersByProcessIdSuccess(){
        Long processId = 1L;
        Trigger trigger = createTrigger(1L, "Test Trigger", createOrganization(1L, "Test Org"), createProcess(processId, "Test Process"));
        when(triggerRepository.findByTargetProcessId(processId)).thenReturn(List.of(trigger));

        Iterable<OrgTriggerPair> triggers = triggerService.getTriggersByProcessId(processId);

        assertNotNull(triggers);
        assertEquals(1, ((Collection<?>) triggers).size());
    }

    /**
     * Test case for retrieving triggers by process ID when no triggers are found.
     */
    @Test
    void getTriggersByProcessIdEmpty() {
        Long processId = 1L;
        when(triggerRepository.findByTargetProcessId(processId)).thenReturn(Collections.emptyList());

        Iterable<OrgTriggerPair> triggers = triggerService.getTriggersByProcessId(processId);

        assertNotNull(triggers);
        assertEquals(0, ((Collection<?>) triggers).size());
    }   

    /**
     * Test case for creating a trigger successfully.
     */
    @Test
    void createTriggerSuccess(){
        TriggerDTO triggerDTO = new TriggerDTO(
            "Test Trigger", 
            "Type A", 
            true, 
            null, 
            1L, 
            1L
        );
        Organization sourceOrganization = createOrganization(1L, "Test Org");
        ProcessEntity targetProcess = createProcess(1L, "Test Process");
        Trigger trigger = createTrigger(1L, "Test Trigger", sourceOrganization, targetProcess);

        when(organizationRepository.getReferenceById(triggerDTO.source())).thenReturn(sourceOrganization);
        when(processService.getProcessById(triggerDTO.target())).thenReturn(targetProcess);
        when(triggerRepository.save(any(Trigger.class))).thenReturn(trigger);
        
        Trigger createdTrigger = triggerService.createTrigger(triggerDTO);

        assertNotNull(createdTrigger);
        assertEquals(trigger.getId(), createdTrigger.getId());
        assertEquals(trigger.getName(), createdTrigger.getName());
        verify(organizationRepository, times(1)).getReferenceById(triggerDTO.source());
        verify(processService, times(1)).getProcessById(triggerDTO.target());
        verify(triggerRepository, times(1)).save(any(Trigger.class));
    }

    /**
     * Test case for creating a trigger when the source organization is not found.
     */
    @Test
    void createTriggerSourceOrganizationNotFound() {
        TriggerDTO triggerDTO = new TriggerDTO(
            "Test Trigger", 
            "Type A", 
            true, 
            null, 
            1L, 
            1L
        );

        when(organizationRepository.getReferenceById(triggerDTO.source())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            triggerService.createTrigger(triggerDTO);
        });
    }

    /**
     * Test case for creating a trigger when the target process is not found.
     */
    @Test
    void createTriggerTargetProcessNotFound() {
        TriggerDTO triggerDTO = new TriggerDTO(
            "Test Trigger", 
            "Type A", 
            true, 
            null, 
            1L, 
            1L
        );
        Organization sourceOrganization = createOrganization(1L, "Test Org");

        when(organizationRepository.getReferenceById(triggerDTO.source())).thenReturn(sourceOrganization);
        when(processService.getProcessById(triggerDTO.target())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            triggerService.createTrigger(triggerDTO);
        });
    }
}
