package com.backend.dashboard_tool.controller;

import java.util.List;
import com.backend.dashboard_tool.entity.People.Organization;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.sipocrecords.ResultOrgPair;
import com.backend.dashboard_tool.entity.Process_Data.Result;
import com.backend.dashboard_tool.service.ResultService;
import com.backend.dashboard_tool.DTO.ResultDTO;
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

@WebMvcTest(ResultController.class)
@AutoConfigureMockMvc(addFilters = false)
class ResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResultService resultService;

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
     * Creates a mock Result object with the given ID, name, organization, and process.
     * @param id
     * @param name
     * @param org
     * @param process
     * @return mock Result object
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
     * Tests the getResults method of the ResultController.
     * This method verifies that the controller correctly retrieves results based on a process ID.
     * @param processId the ID of the process for which to retrieve results
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getResultsSuccess() throws Exception {
        // Create mock data
        Long processId = 1L;
        Organization org = createOrganization(1L, "Org");
        ProcessEntity process = createProcess(processId, "Proc");
        Result result = createResult(1L, "Result", org, process);
        ResultOrgPair pair = new ResultOrgPair(result, org);

        // Mock the service call
        when(resultService.getResultsByProcessId(processId)).thenReturn(List.of(pair));

        // Perform the request
        RequestBuilder request = get("/result/outputconsumer")
                .param("processId", processId.toString())
                .accept(MediaType.APPLICATION_JSON);

        // Execute the request and verify the response
        MvcResult requestResult = mockMvc.perform(request).andReturn();

        // Assert expected json with actual json
        String expectedJson = objectMapper.writeValueAsString(List.of(pair));
        String actualJson = requestResult.getResponse().getContentAsString();

        assertEquals(expectedJson, actualJson);

        // Verify that the service was called with the correct process ID
        verify(resultService, times(1)).getResultsByProcessId(processId);
    }

    /**
     * Tests the getResults method of the ResultController.
     * This method verifies that the controller correctly retrieves results based on a process ID.
     * The result should be a 400 Bad Request if the service throws an exception.
     * @param processId the ID of the process for which to retrieve results
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getResultsFail() throws Exception {
        // Create a mock process ID
        Long processId = 1L;

        // Mock the service call to throw an exception
        when(resultService.getResultsByProcessId(processId)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // Perform the request
        RequestBuilder request = get("/result/outputconsumer")
                .param("processId", processId.toString())
                .accept(MediaType.APPLICATION_JSON);

        // Execute the request and verify the response
        MvcResult requestResult = mockMvc.perform(request).andReturn();

        // Assert that the response status is 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST.value(), requestResult.getResponse().getStatus());
        verify(resultService, times(1)).getResultsByProcessId(processId);
    }

    /**
     * Tests the getResults method of the ResultController.
     * This method verifies that the controller correctly handles a null response from the service.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getResultsNull() throws Exception {
        // Create a mock process ID
        Long processId = 1L;

        // Mock the service call to return null
        when(resultService.getResultsByProcessId(processId)).thenReturn(null);

        // Perform the request
        RequestBuilder request = get("/result/outputconsumer")
                .param("processId", processId.toString())
                .accept(MediaType.APPLICATION_JSON);

        // Execute the request and verify the response
        MvcResult result = mockMvc.perform(request).andReturn();

        // Assert that the response status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        verify(resultService, times(1)).getResultsByProcessId(processId);
    }

    /**
     * Tests the createResult method of the ResultController.
     * This method verifies that the controller correctly creates a result and returns a 201 Created status.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void createResultSuccess() throws Exception {
        // Create a mock ResultDTO and corresponding entities
        ResultDTO resultDTO = new ResultDTO("Test Result", "Type", true, "Description", 1L, 1L);
        Organization org = createOrganization(1L, "Org");
        ProcessEntity process = createProcess(1L, "Proc");
        Result result = createResult(1L, "Test Result", org, process);

        // Mock the service call to return the created result
        when(resultService.createResult(any(ResultDTO.class))).thenReturn(result);

        // Perform the request to create the result
        MvcResult requestResult = mockMvc.perform(MockMvcRequestBuilders.post("/result/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resultDTO)))
                .andReturn();

        // Assert that the response status is 201 Created
        assertEquals(HttpStatus.CREATED.value(), requestResult.getResponse().getStatus());

        // Assert that the response body contains the created result
        verify(resultService, times(1)).createResult(resultDTO);
    }

    /**
     * Tests the createResult method of the ResultController.
     * This method verifies that the controller correctly handles a failure when creating a result.
     * The result should be a 400 Bad Request if the service throws an exception.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void createResultFail() throws Exception {
        // Create a mock ResultDTO
        ResultDTO resultDTO = new ResultDTO("Test Result", "Type", true, "Description", 1L, 1L);

        // Mock the service call to throw an exception
        when(resultService.createResult(any(ResultDTO.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // Perform the request to create the result
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/result/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resultDTO)))
                .andReturn();

        // Assert that the response status is 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        // Verify that the service was called with the correct ResultDTO
        verify(resultService, times(1)).createResult(resultDTO);
    }

    /**
     * Tests the createResult method of the ResultController.
     * This method verifies that the controller correctly handles a failure when creating a result.
     * The result should be a 400 Bad Request if the service throws an exception.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void createResultNull() throws Exception {
        // Create a mock ResultDTO
        ResultDTO resultDTO = new ResultDTO("Test Result", "Type", true, "Description", 1L, 1L);

        // Mock the service call to return null
        when(resultService.createResult(any(ResultDTO.class))).thenReturn(null);

        // Perform the request to create the result
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/result/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resultDTO)))
                .andReturn();

        // Assert that the response status is 404 Not Found
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        // Verify that the service was called with the correct ResultDTO
        verify(resultService, times(1)).createResult(resultDTO);
    }
}