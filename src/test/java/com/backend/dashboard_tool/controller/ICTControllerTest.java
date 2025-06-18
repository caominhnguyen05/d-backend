package com.backend.dashboard_tool.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import com.backend.dashboard_tool.entity.Process_Data.DataStore;
import com.backend.dashboard_tool.service.ICTService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ICTController.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class ICTControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICTService ictService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private DataStore createDataStore(Long id, String name) {
        DataStore ds = new DataStore();
        ds.setId(id);
        ds.setName(name);
        return ds;
    }

    // --- /all endpoint tests ---

    @Test
    void getAllDataStores() throws Exception {
        List<DataStore> dataStores = List.of(createDataStore(1L, "DataStore 1"));
        when(ictService.getAllIctsByType("datastore")).thenReturn((List) dataStores);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ict/all?type=datastore")).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(dataStores), result.getResponse().getContentAsString());
        verify(ictService, times(1)).getAllIctsByType("datastore");
    }

    @Test
    void getAllIctsByType_UnknownType() throws Exception {
        when(ictService.getAllIctsByType("unknown")).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown ICT type: unknown"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ict/all?type=unknown")).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getAllIctsByTypeEmpty() throws Exception {
        when(ictService.getAllIctsByType("datastore")).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ict/all?type=datastore")).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }


    // --- /process endpoint tests ---

    @Test
    void getDataStoresByProcessId() throws Exception {
        Long processId = 10L;
        List<DataStore> dataStores = List.of(createDataStore(1L, "DataStore 1"));
        when(ictService.getIctsByTypeAndProcessId("datastore", processId)).thenReturn((List) dataStores);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ict/process?type=datastore&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(dataStores), result.getResponse().getContentAsString());
        verify(ictService, times(1)).getIctsByTypeAndProcessId("datastore", processId);
    }

    @Test
    void getIctsByTypeAndProcessId_UnknownType() throws Exception {
        when(ictService.getIctsByTypeAndProcessId("unknown", 99L)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown ICT type: unknown"));
        Long processId = 99L;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ict/process?type=unknown&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getIctsByTypeAndProcessIdEmpty() throws Exception {
        Long processId = 99L;
        when(ictService.getIctsByTypeAndProcessId("datastore", processId)).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ict/process?type=datastore&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}
