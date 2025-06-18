package com.backend.dashboard_tool.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.DTO.DataflowDTO;
import com.backend.dashboard_tool.entity.Process_Data.Dataflow;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.service.DataflowService;
import com.backend.dashboard_tool.sipocrecords.DataflowProcessPair;
import com.backend.dashboard_tool.sipocrecords.ProcessDataflowPair;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for DataflowController.
 * This class contains unit tests for the DataflowController methods.
 */
@WebMvcTest(DataflowController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DataflowControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private DataflowService dataflowService;

    private ObjectMapper objectMapper = new ObjectMapper();


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
     * Test method for the getDataflows() method in DataflowController.
     * This test checks if the method returns the expected dataflows based on the provided parameters.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void successfulFilter() throws Exception {
        //Mock the process service to return a list of processes
        List<ProcessEntity> processes = new ArrayList<>();
        ProcessEntity p = createProcessEntity(1L, "Facilitating", 0);
        ProcessEntity p2 = createProcessEntity(123L, "Facilitating", 0);
        processes.add(p);
        processes.add(p2);

        //Mock the dataflow repository to return a list of dataflows
        List<Dataflow> dataflows = new ArrayList<>();
        Dataflow d = new Dataflow();
        d.setId(1L);
        d.setTargetProcess(p);
        d.setSourceProcess(p2);
        dataflows.add(d);

        when(dataflowService.getDataflows(null, "Facilitating")).thenReturn(dataflows);

        //Get the request and perform the request
        RequestBuilder request = MockMvcRequestBuilders.get("/dataflow/filter?level=0&type=Facilitating");
        MvcResult result = mockMvc.perform(request).andReturn();

        //Map the expected dataflows to a string
        String expectedJson = objectMapper.writeValueAsString(dataflows);

        //Get the actual response as JSON then convert it to a string
        String actualJson = result.getResponse().getContentAsString();

        //Compare the expected and actual response
        assertEquals(expectedJson, actualJson);

        verify(dataflowService, times(1)).getDataflows(null, "Facilitating");
    }

    /**
     * Test method for the getDataflows() method in DataflowController.
     * This test checks if the method returns a 400 BAD REQUEST status when an error occurs.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void failedFilter() throws Exception {
        //Mock the process service to throw a ResponseStatusException
        when(dataflowService.getDataflows(null, null)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        //Perform the request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/dataflow/filter?level=1"))
                                  .andReturn();

        //Check that the response status is 400 BAD REQUEST
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        //Verify that the process service was called once with the correct parameters
        verify(dataflowService, times(1)).getDataflows(null, null);
    }

    /**
     * Test method for the createDataflow() method in DataflowController.
     * This test checks if the method creates a new dataflow successfully.
     * @param dataflowDTO the DataflowDTO object to create
     * @return the created Dataflow object
     * @throws Exception if an error occurs during the test
     */
    @Test
    void createDataflowSuccess() throws Exception {
        //Mock the dataflow service to return a new dataflow
        DataflowDTO dataflowDTO = new DataflowDTO("Test dataflow", null, null, null, 1L, 2L);

        when(dataflowService.createDataflow(dataflowDTO)).thenReturn(new Dataflow());

        //Perform the request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/dataflow/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dataflowDTO)))
                .andReturn();

        //Check that the response status is 201 CREATED
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());

        //Verify that the dataflow service was called once with the correct parameters
        verify(dataflowService, times(1)).createDataflow(dataflowDTO);
    }

    
    /**
     * Test method for the createDataflow() method in DataflowController.
     * This test checks if the method creates a new dataflow successfully.
     * @param dataflowDTO the DataflowDTO object to create
     * @return the created Dataflow object
     * @throws Exception if an error occurs during the test
     */
    @Test
    void createDataflowFail() throws Exception {
        //Mock the dataflow service to return a new dataflow
        DataflowDTO dataflowDTO = new DataflowDTO("Test dataflow", null, null, null, 1L, 2L);

        when(dataflowService.createDataflow(dataflowDTO)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        //Perform the request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/dataflow/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dataflowDTO)))
                .andReturn();

        //Check that the response status is 400 BAD REQUEST
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        //Verify that the dataflow service was called once with the correct parameters
        verify(dataflowService, times(1)).createDataflow(dataflowDTO);
    }

    /**
     * Test method for the createDataflow() method in DataflowController.
     * This test checks if the method creates a new dataflow but returns null.
     * @param dataflowDTO the DataflowDTO object to create
     * @return the created Dataflow object
     * @throws Exception if an error occurs during the test
     */
    @Test
    void createDataflowNull() throws Exception {
        //Mock the dataflow service to return null
        DataflowDTO dataflowDTO = new DataflowDTO("Test dataflow", null, null, null, 1L, 2L);

        when(dataflowService.createDataflow(dataflowDTO)).thenReturn(null);

        //Perform the request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/dataflow/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dataflowDTO)))
                .andReturn();

        //Check that the response status is 400 BAD REQUEST
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        //Verify that the dataflow service was called once with the correct parameters
        verify(dataflowService, times(1)).createDataflow(dataflowDTO);
    }

    /**
     * Test method for the getInpuntDataflows() method in DataflowController.
     * This test checks if the method returns the expected input dataflows based on the provided process ID and type with expected usage. 
     * It checks if the result returns correct information in the correct format.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void inputDataflowsSuccessful() throws Exception {
        List<ProcessDataflowPair> results = new ArrayList<>();
        List<Dataflow> dataflows = new ArrayList<>();
        ProcessEntity processOne = createProcessEntity(1L, "Facilitating", 0);
        ProcessEntity processTwo = createProcessEntity(2L, "Facilitating", 0);
        ProcessEntity processThree = createProcessEntity(3L, "Primary", 0);
        ProcessEntity processFour = createProcessEntity(4L, "Primary", 0);

        Dataflow dataflowOne = new Dataflow();
        dataflowOne.setId(1L);
        dataflowOne.setSourceProcess(processOne);
        dataflowOne.setTargetProcess(processFour);
        dataflows.add(dataflowOne);

        Dataflow dataflowTwo = new Dataflow();
        dataflowTwo.setId(2L);
        dataflowTwo.setSourceProcess(processThree);
        dataflowTwo.setTargetProcess(processFour);
        dataflows.add(dataflowTwo);

        Dataflow dataflowThree = new Dataflow();
        dataflowThree.setId(3L);
        dataflowThree.setSourceProcess(processTwo);
        dataflowThree.setTargetProcess(processFour);
        dataflows.add(dataflowThree);

        results.add(new ProcessDataflowPair(processOne, dataflowOne));
        results.add(new ProcessDataflowPair(processTwo, dataflowThree));

        when(dataflowService.getInputDataflows(4L, null, "Primary")).thenReturn(results);

        String expectedJson = objectMapper.writeValueAsString(results);
        String actualJson = mockMvc.perform(MockMvcRequestBuilders.get("/dataflow/inputs?processId=4&type=Primary"))
                .andReturn().getResponse().getContentAsString();

        assertEquals(expectedJson, actualJson);
        verify(dataflowService, times(1)).getInputDataflows(4L, null, "Primary");
    }


    /**
     * Test method for the getInputDataflows() method in DataflowController.
     * This test checks if the method returns an empty list when all input dataflows are filtered out based on the provided process ID and type.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void inputDataflowsAllFilteredOut() throws Exception {
        List<Dataflow> dataflows = new ArrayList<>();
        ProcessEntity processA = createProcessEntity(1L, "Primary", 0);
        ProcessEntity processB = createProcessEntity(2L, "Primary", 0);
        ProcessEntity processTarget = createProcessEntity(3L, "Primary", 0);
    
        Dataflow df1 = new Dataflow();
        df1.setId(1L);
        df1.setSourceProcess(processA);
        df1.setTargetProcess(processTarget);
        dataflows.add(df1);
    
        Dataflow df2 = new Dataflow();
        df2.setId(2L);
        df2.setSourceProcess(processB);
        df2.setTargetProcess(processTarget);
        dataflows.add(df2);
    
        when(dataflowService.getInputDataflows(3L, null, "Primary")).thenReturn(Collections.emptyList());
    
        RequestBuilder request = MockMvcRequestBuilders.get("/dataflow/inputs?processId=3&type=Primary");
        MvcResult result = mockMvc.perform(request).andReturn();
    
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    /**
     * Test method for the getInputDataflows() method in DataflowController.
     * This test checks if the method returns a 400 BAD REQUEST status when the required parameters are missing or in incorrect format.
     * @throws Exception
     */
    @Test
    void inputDataflowsMissingParams() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/dataflow/inputs");
        MvcResult result = mockMvc.perform(request).andReturn();
    
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        RequestBuilder requestWithMissingType = MockMvcRequestBuilders.get("/dataflow/inputs?processId=3");
        MvcResult resultWithMissingType = mockMvc.perform(requestWithMissingType).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), resultWithMissingType.getResponse().getStatus());

        RequestBuilder requestWithMissingProcessId = MockMvcRequestBuilders.get("/dataflow/inputs?type=Primary");
        MvcResult resultWithMissingProcessId = mockMvc.perform(requestWithMissingProcessId).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), resultWithMissingProcessId.getResponse().getStatus());

        RequestBuilder requestWithInvalidProcessId = MockMvcRequestBuilders.get("/dataflow/inputs?processId=invalid&type=Primary");
        MvcResult resultWithInvalidProcessId = mockMvc.perform(requestWithInvalidProcessId).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), resultWithInvalidProcessId.getResponse().getStatus());

    }

        /**
     * Test method for the getOutputDataflows() method in DataflowController.
     * This test checks if the method returns the expected output dataflows based on the provided process ID and type.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void outputDataflowsSuccessful() throws Exception {
        // Arrange
        Long processId = 5L;
        String type = "Primary";
        ProcessEntity processA = createProcessEntity(1L, "Primary", 0);
        ProcessEntity processB = createProcessEntity(2L, "Primary", 0);

        Dataflow dataflowA = new Dataflow();
        dataflowA.setId(1L);
        dataflowA.setSourceProcess(processA);
        dataflowA.setTargetProcess(processB);

        DataflowProcessPair pair = new DataflowProcessPair(dataflowA, processB);
        List<DataflowProcessPair> expectedPairs = List.of(pair);

        when(dataflowService.getOutputDataflows(processId, null, type)).thenReturn(expectedPairs);

        // Act
        String url = "/dataflow/outputs?processId=" + processId + "&type=" + type;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn();

        // Assert
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(expectedPairs), result.getResponse().getContentAsString());
        verify(dataflowService, times(1)).getOutputDataflows(processId, null,type);
    }

    /**
     * Test method for the getOutputDataflows() method in DataflowController.
     * This test checks if the method returns an empty list when no output dataflows are found.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void outputDataflowsEmpty() throws Exception {
        Long processId = 6L;
        String type = "Facilitating";
        when(dataflowService.getOutputDataflows(processId, null, type)).thenReturn(List.of());

        String url = "/dataflow/outputs?processId=" + processId + "&type=" + type;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[]", result.getResponse().getContentAsString());
        verify(dataflowService, times(1)).getOutputDataflows(processId, null,type);
    }

    /**
     * Test method for the getOutputDataflows() method in DataflowController.
     * This test checks if the method returns a 400 BAD REQUEST status when required parameters are missing or invalid.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void outputDataflowsMissingParams() throws Exception {
        // Missing both params
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get("/dataflow/outputs")).andReturn();
        assertEquals(400, result1.getResponse().getStatus());

        // Missing type
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/dataflow/outputs?processId=1")).andReturn();
        assertEquals(400, result2.getResponse().getStatus());

        // Missing processId
        MvcResult result3 = mockMvc.perform(MockMvcRequestBuilders.get("/dataflow/outputs?type=Primary")).andReturn();
        assertEquals(400, result3.getResponse().getStatus());

        // Invalid processId
        MvcResult result4 = mockMvc.perform(MockMvcRequestBuilders.get("/dataflow/outputs?processId=invalid&type=Primary")).andReturn();
        assertEquals(400, result4.getResponse().getStatus());
    }
}

