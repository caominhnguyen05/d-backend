package com.backend.dashboard_tool.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.backend.dashboard_tool.database.DataflowRepository;
import com.backend.dashboard_tool.entity.Process_Data.Dataflow;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.sipocrecords.DataflowProcessPair;
import com.backend.dashboard_tool.sipocrecords.ProcessDataflowPair;
import com.backend.dashboard_tool.DTO.DataflowDTO;


@ExtendWith(MockitoExtension.class)
public class DataflowServiceTest {
    @Mock
    private DataflowRepository dataflowRepository;
    @Mock
    private ProcessService processService;
    @InjectMocks
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

        when(processService.getSubProcesses(null, "Facilitating")).thenReturn(processes);

        //Mock the dataflow repository to return a list of dataflows
        List<Dataflow> dataflows = new ArrayList<>();
        Dataflow d = new Dataflow();
        d.setId(1L);
        d.setTargetProcess(p);
        d.setSourceProcess(p2);
        dataflows.add(d);

        when(dataflowRepository.findByProcessIds(any())).thenReturn(dataflows);

        //Get the request and perform the request
        Iterable<Dataflow> result = dataflowService.getDataflows(null, "Facilitating");

        //Map the expected dataflows to a string
        String expectedJson = objectMapper.writeValueAsString(dataflows);

        //Get the actual response as JSON then convert it to a string
        String actualJson = objectMapper.writeValueAsString(result);

        //Compare the expected and actual response
        assertEquals(expectedJson, actualJson);

        // Verify that the dataflow repository was called with the correct process IDs
        verify(dataflowRepository).findByProcessIds(any());

        // Verify that the process service was called with the correct parameters
        verify(processService).getSubProcesses(null, "Facilitating");
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
        when(processService.getSubProcesses(null, null)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));


        assertThrows(ResponseStatusException.class, () -> {
            //Call the getDataflows method with invalid parameters
            dataflowService.getDataflows(null, null);
        });

        //Verify that the process service was called once with the correct parameters
        verify(processService, times(1)).getSubProcesses(null, null);

        //Verify that the dataflow repository was never called
        verify(dataflowRepository, times(0)).findAll();
    }

    /**
     * Test method for the createDataflow() method in DataflowService.
     * This test checks if the method successfully creates a dataflow with valid parameters.
     */
    @Test
    void createDataflowSuccessSameTypeSameLevel(){
        // Create a DataflowDTO object with valid parameters
        DataflowDTO dataflowDTO = new DataflowDTO("Test Dataflow", null, null, "Test Description", 1L, 2L);

        // Mock the process service to return the source and target processes
        ProcessEntity sourceProcess = createProcessEntity(1L, "Facilitating", 0);
        ProcessEntity targetProcess = createProcessEntity(2L, "Facilitating", 0);

        Dataflow dataflow = new Dataflow();
        dataflow.setId(null); // ID will be generated by the repository
        dataflow.setSourceProcess(sourceProcess);
        dataflow.setTargetProcess(targetProcess);
        dataflow.setName(dataflowDTO.name());
        dataflow.setDescription(dataflowDTO.description());
        dataflow.setLevel(sourceProcess.getLevel());
        dataflow.setType(sourceProcess.getType());
        
        when(processService.getProcessById(1L)).thenReturn(sourceProcess);
        when(processService.getProcessById(2L)).thenReturn(targetProcess);
        when(dataflowRepository.save(Mockito.any(Dataflow.class))).thenReturn(dataflow);

        // Call the createDataflow method
        Dataflow resultDataflow = dataflowService.createDataflow(dataflowDTO);

        // Verify that the dataflow repository was called to save the new dataflow
        verify(dataflowRepository).save(Mockito.any(Dataflow.class));

        assertEquals(dataflow, resultDataflow);
    }

    /**
     * Test method for the createDataflow() method in DataflowService.
     * This test checks if the method successfully creates a dataflow with valid parameters.
     */
    @Test
    void createDataflowSuccessDifferentTypes(){
        // Create a DataflowDTO object with valid parameters
        DataflowDTO dataflowDTO = new DataflowDTO("Test Dataflow", null, null, "Test Description", 1L, 2L);

        // Mock the process service to return the source and target processes
        ProcessEntity sourceProcess = createProcessEntity(1L, "Facilitating", 0);
        ProcessEntity targetProcess = createProcessEntity(2L, "Primary", 2);

        Dataflow dataflow = new Dataflow();
        dataflow.setId(null); // ID will be generated by the repository
        dataflow.setSourceProcess(sourceProcess);
        dataflow.setTargetProcess(targetProcess);
        dataflow.setName(dataflowDTO.name());
        dataflow.setDescription(dataflowDTO.description());
        dataflow.setLevel(sourceProcess.getLevel());
        dataflow.setType(sourceProcess.getType());

        when(processService.getProcessById(1L)).thenReturn(sourceProcess);
        when(processService.getProcessById(2L)).thenReturn(targetProcess);
        when(dataflowRepository.save(Mockito.any(Dataflow.class))).thenReturn(dataflow);

        // Call the createDataflow method
        Dataflow resultDataflow = dataflowService.createDataflow(dataflowDTO);

        // Verify that the dataflow repository was called to save the new dataflow
        verify(dataflowRepository).save(Mockito.any(Dataflow.class));

        assertEquals(dataflow, resultDataflow);
    }

    @Test
    void createDataflowSameIds(){
        // Create a DataflowDTO object with the same source and target process IDs
        DataflowDTO dataflowDTO = new DataflowDTO("Test Dataflow", null, null, "Test Description", 1L, 1L);

        // Call the createDataflow method and expect a ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {
            dataflowService.createDataflow(dataflowDTO);
        });

        // Verify that the dataflow repository was never called
        verify(dataflowRepository, times(0)).save(Mockito.any(Dataflow.class));
    }

    @Test
    void createDataflowSourceNotFound(){
        // Create a DataflowDTO object with a non-existing source process ID
        DataflowDTO dataflowDTO = new DataflowDTO("Test Dataflow", null, null, "Test Description", 1L, 2L);

        // Mock the process service to return null for the non-existing source process
        when(processService.getProcessById(1L)).thenReturn(null);

        // Call the createDataflow method and expect a ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {
            dataflowService.createDataflow(dataflowDTO);
        });

        // Verify that the dataflow repository was never called
        verify(dataflowRepository, times(0)).save(Mockito.any(Dataflow.class));
    }

    @Test 
    void createDataflowTargetNotFound(){
        // Create a DataflowDTO object with a non-existing target process ID
        DataflowDTO dataflowDTO = new DataflowDTO("Test Dataflow", null, null, "Test Description", 1L, 2L);

        // Mock the process service to return null for the non-existing target process
        when(processService.getProcessById(1L)).thenReturn(createProcessEntity(1L, "Facilitating", 0));
        when(processService.getProcessById(2L)).thenReturn(null);

        // Call the createDataflow method and expect a ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {
            dataflowService.createDataflow(dataflowDTO);
        });

        // Verify that the dataflow repository was never called
        verify(dataflowRepository, times(0)).save(Mockito.any(Dataflow.class));
    }

        /**
     * Test method for the getInputDataflows() method in DataflowController.
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
        results.add(new ProcessDataflowPair(processThree, dataflowTwo));
        results.add(new ProcessDataflowPair(processTwo, dataflowThree));

        when(dataflowRepository.findInputDataflows(4L, null,"Primary")).thenReturn(dataflows);

        Iterable<ProcessDataflowPair> dataflowPair = dataflowService.getInputDataflows(4L, null,"Primary");
        String actualJson = objectMapper.writeValueAsString(dataflowPair);
        String expectedJson = objectMapper.writeValueAsString(results);
        assertEquals(expectedJson, actualJson);
        verify(dataflowRepository, times(1)).findInputDataflows(4L, null,"Primary");
        verify(dataflowRepository, times(0)).findBySourceProcessId(any());
        verify(dataflowRepository, times(0)).findAll();
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
    
        when(dataflowService.getInputDataflows(3L, null,"Primary")).thenReturn(Collections.emptyList());

        Iterable<ProcessDataflowPair> dataflowPair = dataflowService.getInputDataflows(3L, null,"Primary");
        String actualJson = objectMapper.writeValueAsString(dataflowPair);

        assertEquals("[]", actualJson);
    }

    /**
     * Test method for the getInputDataflows() method in DataflowController.
     * This test checks if the method returns an empty list when no input dataflows are found in the database for the specified process ID.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void inputDataflowsNoneFound() throws Exception {
        when(dataflowRepository.findInputDataflows(99L, null,"NoFilter")).thenReturn(Collections.emptyList());

        Iterable<ProcessDataflowPair> dataflowPair = dataflowService.getInputDataflows(99L,null, "NoFilter");
        String actualJson = objectMapper.writeValueAsString(dataflowPair);

        assertEquals("[]", actualJson);
    }


    /**
     * Test method for the getOutputDataflows() method in DataflowController.
     * This test checks if the method returns the expected output dataflows based on the provided process ID and type with expected usage. 
     * It checks if the result returns correct information in the correct format.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void outputDataflowsSuccessful() throws Exception {
        List<DataflowProcessPair> results = new ArrayList<>();
        List<Dataflow> dataflows = new ArrayList<>();
        ProcessEntity processOne = createProcessEntity(1L, "Facilitating", 0);
        ProcessEntity processTwo = createProcessEntity(2L, "Facilitating", 0);
        ProcessEntity processThree = createProcessEntity(3L, "Primary", 0);
        ProcessEntity processFour = createProcessEntity(4L, "Primary", 0);

        Dataflow dataflowOne = new Dataflow();
        dataflowOne.setId(1L);
        dataflowOne.setSourceProcess(processFour);
        dataflowOne.setTargetProcess(processOne);
        dataflows.add(dataflowOne);

        Dataflow dataflowTwo = new Dataflow();
        dataflowTwo.setId(2L);
        dataflowTwo.setSourceProcess(processFour);
        dataflowTwo.setTargetProcess(processThree);
        dataflows.add(dataflowTwo);

        Dataflow dataflowThree = new Dataflow();
        dataflowThree.setId(3L);
        dataflowThree.setSourceProcess(processFour);
        dataflowThree.setTargetProcess(processTwo);
        dataflows.add(dataflowThree);

        results.add(new DataflowProcessPair(dataflowOne, processOne));
        results.add(new DataflowProcessPair(dataflowTwo, processThree));
        results.add(new DataflowProcessPair(dataflowThree, processTwo));

        when(dataflowRepository.findOutputDataflows(4L, null,"Primary")).thenReturn(dataflows);

        Iterable<DataflowProcessPair> dataflowPair = dataflowService.getOutputDataflows(4L, null, "Primary");
        String actualJson = objectMapper.writeValueAsString(dataflowPair);
        String expectedJson = objectMapper.writeValueAsString(results);
        assertEquals(expectedJson, actualJson);
        verify(dataflowRepository, times(1)).findOutputDataflows(4L, null, "Primary");
        verify(dataflowRepository, times(0)).findByTargetProcessId(any());
        verify(dataflowRepository, times(0)).findAll();
    }


    /**
     * Test method for the getOutputDataflows() method in DataflowController.
     * This test checks if the method returns an empty list when all output dataflows are filtered out based on the provided process ID and type.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void outputDataflowsAllFilteredOut() throws Exception {
        List<Dataflow> dataflows = new ArrayList<>();
        ProcessEntity processA = createProcessEntity(1L, "Primary", 0);
        ProcessEntity processB = createProcessEntity(2L, "Primary", 0);
        ProcessEntity processSource = createProcessEntity(3L, "Primary", 0);
    
        Dataflow df1 = new Dataflow();
        df1.setId(1L);
        df1.setTargetProcess(processA);
        df1.setSourceProcess(processSource);
        dataflows.add(df1);
    
        Dataflow df2 = new Dataflow();
        df2.setId(2L);
        df2.setTargetProcess(processB);
        df2.setSourceProcess(processSource);
        dataflows.add(df2);
    
        when(dataflowService.getOutputDataflows(3L, null,"Primary")).thenReturn(Collections.emptyList());

        Iterable<DataflowProcessPair> dataflowPair = dataflowService.getOutputDataflows(3L, null,"Primary");
        String actualJson = objectMapper.writeValueAsString(dataflowPair);

        assertEquals("[]", actualJson);
    }

    /**
     * Test method for the getOutputDataflows() method in DataflowController.
     * This test checks if the method returns an empty list when no Output dataflows are found in the database for the specified process ID.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void outputDataflowsNoneFound() throws Exception {
        when(dataflowRepository.findOutputDataflows(99L, null,"NoFilter")).thenReturn(Collections.emptyList());
    

        Iterable<DataflowProcessPair> dataflowPair = dataflowService.getOutputDataflows(99L, null, "NoFilter");
        String actualJson = objectMapper.writeValueAsString(dataflowPair);

        assertEquals("[]", actualJson);
    }


}
