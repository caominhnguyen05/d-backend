package com.backend.dashboard_tool.controller;

import java.util.List;
import com.backend.dashboard_tool.DTO.TriggerDTO;
import com.backend.dashboard_tool.entity.People.Organization;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Process_Data.Trigger;
import com.backend.dashboard_tool.service.TriggerService;
import com.backend.dashboard_tool.sipocrecords.OrgTriggerPair;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(TriggerController.class)
@AutoConfigureMockMvc(addFilters = false)
class TriggerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TriggerService triggerService;

    @Autowired
    private ObjectMapper objectMapper;

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
     * Tests the getTriggers method of the TriggerController.
     * This method verifies that the controller correctly retrieves triggers based on a process ID.
     * @param processId the ID of the process for which to retrieve triggers
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getTriggersSuccess() throws Exception {
        // Create mock data
        Long processId = 1L;
        Organization org = createOrganization(1L, "Org");
        ProcessEntity process = createProcess(processId, "Proc");
        Trigger trigger = createTrigger(1L, "Trig", org, process);
        OrgTriggerPair pair = new OrgTriggerPair(org, trigger);

        // Mock the service call
        when(triggerService.getTriggersByProcessId(processId)).thenReturn(List.of(pair));

        // Perform the request
        RequestBuilder request = get("/trigger/supplierinput")
                .param("processId", processId.toString())
                .accept(MediaType.APPLICATION_JSON);

        // Execute the request and verify the response
        MvcResult result = mockMvc.perform(request).andReturn();

        // Assert expected json with actual json
        String expectedJson = objectMapper.writeValueAsString(List.of(pair));
        String actualJson = result.getResponse().getContentAsString();
        
        assertEquals(expectedJson, actualJson);

        // Verify that the service was called with the correct process ID
        verify(triggerService, times(1)).getTriggersByProcessId(processId);
    }

    /**
     * Tests the getTriggers method of the TriggerController.
     * This method verifies that the controller correctly retrieves triggers based on a process ID.
     * The result should be a 400 Bad Request if the service throws an exception.
     * @param processId the ID of the process for which to retrieve triggers
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getTriggersFail() throws Exception {
        // Create a mock process ID
        Long processId = 1L;

        // Mock the service call to throw an exception
        when(triggerService.getTriggersByProcessId(processId)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // Perform the request
        RequestBuilder request = get("/trigger/supplierinput")
                .param("processId", processId.toString())
                .accept(MediaType.APPLICATION_JSON);

        // Execute the request and verify the response
        MvcResult result = mockMvc.perform(request).andReturn();

        // Assert that the response status is 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        verify(triggerService, times(1)).getTriggersByProcessId(processId);
    }

    /**
     * Tests the getTriggers method of the TriggerController.
     * This method verifies that the controller correctly handles a null response from the service.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getTriggersNull() throws Exception {
        // Create a mock process ID
        Long processId = 1L;

        // Mock the service call to return null
        when(triggerService.getTriggersByProcessId(processId)).thenReturn(null);

        // Perform the request
        RequestBuilder request = get("/trigger/supplierinput")
                .param("processId", processId.toString())
                .accept(MediaType.APPLICATION_JSON);

        // Execute the request and verify the response
        MvcResult result = mockMvc.perform(request).andReturn();

        // Assert that the response status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        verify(triggerService, times(1)).getTriggersByProcessId(processId);
    }

    /**
     * Tests the createTrigger method of the TriggerController.
     * This method verifies that the controller correctly creates a trigger and returns a 201 Created status.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void createTriggerSuccess() throws Exception {
        // Create a mock TriggerDTO and corresponding entities
        TriggerDTO triggerDTO = new TriggerDTO("Test Trigger", "Type", true, "Description", 1L, 1L);
        Organization org = createOrganization(1L, "Org");
        ProcessEntity process = createProcess(1L, "Proc");
        Trigger trigger = createTrigger(1L, "Test Trigger", org, process);

        // Mock the service call to return the created trigger
        when(triggerService.createTrigger(any(TriggerDTO.class))).thenReturn(trigger);

        // Perform the request to create the trigger
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/trigger/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(triggerDTO)))
                .andReturn();

        // Assert that the response status is 201 Created
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());       

        // Assert that the response body contains the created trigger
        verify(triggerService, times(1)).createTrigger(triggerDTO);
    }

    /**
     * Tests the createTrigger method of the TriggerController.
     * This method verifies that the controller correctly handles a failure when creating a trigger.
     * The result should be a 400 Bad Request if the service throws an exception.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void createTriggerFail() throws Exception {
        // Create a mock TriggerDTO
        TriggerDTO triggerDTO = new TriggerDTO("Test Trigger", "Type", true, "Description", 1L, 1L);

        // Mock the service call to throw an exception
        when(triggerService.createTrigger(any(TriggerDTO.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // Perform the request to create the trigger
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/trigger/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(triggerDTO)))
                .andReturn();

        // Assert that the response status is 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        // Verify that the service was called with the correct TriggerDTO
        verify(triggerService, times(1)).createTrigger(triggerDTO);
    }

    /**
     * Tests the createTrigger method of the TriggerController.
     * This method verifies that the controller correctly handles a failure when creating a trigger.
     * The result should be a 400 Bad Request if the service throws an exception.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void createTriggerNull() throws Exception {
        // Create a mock TriggerDTO
        TriggerDTO triggerDTO = new TriggerDTO("Test Trigger", "Type", true, "Description", 1L, 1L);

        // Mock the service call to return null
        when(triggerService.createTrigger(any(TriggerDTO.class))).thenReturn(null);

        // Perform the request to create the trigger
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/trigger/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(triggerDTO)))
                .andReturn();

        // Assert that the response status is 404 Not Found
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        // Verify that the service was called with the correct TriggerDTO
        verify(triggerService, times(1)).createTrigger(triggerDTO);
    }
}