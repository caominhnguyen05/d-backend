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

import com.backend.dashboard_tool.entity.Process_Data.Improvement;
import com.backend.dashboard_tool.service.InitiativeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(InitiativeController.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class InitiativeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InitiativeService initiativeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Improvement createImprovement(Long id, String name) {
        Improvement improvement = new Improvement();
        improvement.setId(id);
        improvement.setName(name);
        return improvement;
    }
    // --- /all endpoint tests ---

    @Test
    void getAllImprovements() throws Exception {
        List<Improvement> improvements = List.of(createImprovement(1L, "Improvement 1"));
        when(initiativeService.getAllInitiativesByType("improvement")).thenReturn((List) improvements);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/initiative/all?type=improvement")).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(improvements), result.getResponse().getContentAsString());
        verify(initiativeService, times(1)).getAllInitiativesByType("improvement");
    }

    @Test
    void getAllInitiativesByType_UnknownType() throws Exception {
        when(initiativeService.getAllInitiativesByType("unknown")).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown layer type: unknown"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/initiative/all?type=unknown")).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getAllInitiativesByTypeEmpty() throws Exception {
        when(initiativeService.getAllInitiativesByType("improvement")).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/initiative/all?type=improvement")).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    // --- /process endpoint tests ---

    @Test
    void getImprovementsByProcessId() throws Exception {
        Long processId = 10L;
        List<Improvement> improvements = List.of(createImprovement(1L, "Improvement 1"));
        when(initiativeService.getInitiativesByTypeAndProcessId("improvement", processId)).thenReturn((List) improvements);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/initiative/process?type=improvement&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(improvements), result.getResponse().getContentAsString());
        verify(initiativeService, times(1)).getInitiativesByTypeAndProcessId("improvement", processId);
    }


    @Test
    void getInitiativesByTypeAndProcessId_UnknownType() throws Exception {
        when(initiativeService.getInitiativesByTypeAndProcessId("unknown", 99L)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown layer type: unknown"));

        Long processId = 99L;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/initiative/process?type=unknown&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getInitiativesByTypeAndProcessIdEmpty() throws Exception {
        Long processId = 99L;
        when(initiativeService.getInitiativesByTypeAndProcessId("improvement", processId)).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/initiative/process?type=improvement&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}
