package com.backend.dashboard_tool.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.service.ProcessService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for ProcessController.
 * This class contains unit tests for the ProcessController methods.
 */
@WebMvcTest(ProcessController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProcessControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private ProcessRepository processRepository;

    @MockitoBean
    private ProcessService processService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ProcessEntity createProcessEntity(Long id, String type, int level) {
        ProcessEntity process = new ProcessEntity();
        process.setId(id);
        process.setType(type);
        process.setLevel(level);
        return process;
    }
    
    /**
     * Test for getAllProcesses() method in ProcessController.
     * This method is used to get all processes.
     * It returns a list of all processes.
     * @throws Exception
     */
    @Test
    void getAllProcesses() throws Exception {
        //Mock the process repository to return a list of processes
        List<ProcessEntity> processes = new ArrayList<>();
        ProcessEntity p = createProcessEntity(1L, "Facilitating", 0);
        ProcessEntity p2 = createProcessEntity(123L, "Main", 0);
        processes.add(p);
        processes.add(p2);

        when(processRepository.findAll()).thenReturn(processes);

        //Get the request and perform the request
        RequestBuilder request = MockMvcRequestBuilders.get("/process/all");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Convert the response to a list of ProcessEntity objects and assert to expected list.
        List<ProcessEntity> response = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), ProcessEntity[].class));
        assertEquals(processes, response);

        //Verify that the process repository was called once
        verify(processRepository, times(1)).findAll();
    }

    /**
     * Test for getById() method in ProcessController.
     * This test will test the case when the process is found.
     * @throws Exception
     */
    @Test
    void getByIdSuccess() throws Exception {
        //Mock the process repository to return a process
        ProcessEntity process = createProcessEntity(1L, "Facilitating", 0);

        when(processRepository.findById(1L)).thenReturn(Optional.of(process));

        //Get the request and perform the request
        RequestBuilder request = MockMvcRequestBuilders.get("/process/1");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Convert the response to a ProcessEntity object and assert to expected process.
        ProcessEntity response = objectMapper.readValue(result.getResponse().getContentAsString(), ProcessEntity.class);
        assertEquals(process, response);

        //Verify that the process repository was called once with the correct id
        verify(processRepository, times(1)).findById(1L);
    }

    /**
     * Test for getById() method in ProcessController.
     * This test will test the case when the process is not found.
     * @throws Exception
     */
    @Test
    void getByIdDoesNotExist() throws Exception {
        //Mock the process repository to return an empty optional
        when(processRepository.findById(1L)).thenReturn(Optional.empty());

        //Get the request and perform the request
        RequestBuilder request = MockMvcRequestBuilders.get("/process/1");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert that the response status is 404
        assertEquals(404, result.getResponse().getStatus());

        //Verify that the process repository was not called by any id
        verify(processRepository, times(1)).findById(1L);
    }

    /**
     * Test for createProcess() method in ProcessController.
     * This test will test the case when the process is created successfully.
     * @throws Exception
     */
    @Test
    void createProcessSuccess() throws Exception {
        //Create a process entity to be created
        ProcessEntity process = createProcessEntity(1L, "Facilitating", 0);
        
        //Mock the process service to return the created process
        when(processService.createProcess(any())).thenReturn(process);

        //Convert the process entity to JSON
        String json = objectMapper.writeValueAsString(process);

        //Get the request and perform the request
        RequestBuilder request = MockMvcRequestBuilders.post("/process/create")
                .contentType("application/json")
                .content(json);
        MvcResult result = mockMvc.perform(request).andReturn();

        //Convert the response to a ProcessEntity object and assert to expected process.
        ProcessEntity response = objectMapper.readValue(result.getResponse().getContentAsString(), ProcessEntity.class);
        assertEquals(process, response);

        //Verify that the process service was called once with the correct process
        verify(processService, times(1)).createProcess(any());
    }
    /**
     * Test for createProcess() method in ProcessController.
     * This test will test the case when the process creation fails.
     * @throws Exception
     */
    @Test
    void createProcessFailure() throws Exception {
        //Mock the process service to return null
        when(processService.createProcess(any())).thenReturn(null);

        //Create a process entity to be created
        ProcessEntity process = createProcessEntity(1L, "Facilitating", 0);
        
        //Convert the process entity to JSON
        String json = objectMapper.writeValueAsString(process);

        //Get the request and perform the request
        RequestBuilder request = MockMvcRequestBuilders.post("/process/create")
                .contentType("application/json")
                .content(json);
        MvcResult result = mockMvc.perform(request).andReturn();

        //Assert that the response status is 400
        assertEquals(400, result.getResponse().getStatus());

        //Verify that the process service was called once with the correct process
        verify(processService, times(1)).createProcess(any());
    }

    /**
     * Test for getSubProcesses() method in ProcessController.
     * This test checks if the endpoint returns the expected sub-processes for given parameters.
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getSubProcesses() throws Exception {
        Long parentId = 1L;
        int level = 2;
        String type = "Main";
        List<ProcessEntity> subProcesses = List.of(
            createProcessEntity(10L, "Main", 2),
            createProcessEntity(11L, "Main", 2)
        );

        when(processService.getSubProcesses(parentId, type)).thenReturn(subProcesses);

        // Build the request with all parameters
        RequestBuilder request = MockMvcRequestBuilders.get("/process/subprocess")
            .param("parentId", parentId.toString())
            .param("level", String.valueOf(level))
            .param("type", type);

        MvcResult result = mockMvc.perform(request).andReturn();

        List<ProcessEntity> response = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), ProcessEntity[].class));
        assertEquals(subProcesses, response);
        verify(processService, times(1)).getSubProcesses(parentId, type);
    }

    /**
     * Test for getSubProcesses() method in ProcessController.
     * This test checks if the endpoint works with only the required parameter (level).
     * @throws Exception if an error occurs during the test
     */
    @Test
    void getSubProcesses_OnlyLevel() throws Exception {
        int level = 1;
        List<ProcessEntity> subProcesses = List.of(
            createProcessEntity(20L, "Facilitating", 1)
        );

        when(processService.getSubProcesses(null, null)).thenReturn(subProcesses);

        // Build the request with only the required parameter
        RequestBuilder request = MockMvcRequestBuilders.get("/process/subprocess")
            .param("level", String.valueOf(level));

        MvcResult result = mockMvc.perform(request).andReturn();

        List<ProcessEntity> response = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), ProcessEntity[].class));
        assertEquals(subProcesses, response);
        verify(processService, times(1)).getSubProcesses(null, null);
    }
}
