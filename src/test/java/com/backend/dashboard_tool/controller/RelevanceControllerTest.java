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
import com.backend.dashboard_tool.entity.Finance.Cost;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.dashboard_tool.service.RelevanceService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(RelevanceController.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class RelevanceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RelevanceService relevanceService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Cost createCost(Long id, String name) {
        Cost cost = new Cost();
        cost.setId(id);
        cost.setName(name);
        return cost;
    }

    // --- /all endpoint tests ---
    
    @Test
    void getAllCosts() throws Exception {
        List<Cost> costs = List.of(createCost(1L, "Cost 1"));
        when(relevanceService.getAllRelevancesByType("cost")).thenReturn((List) costs);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/relevance/all?type=cost")).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(costs), result.getResponse().getContentAsString());
        verify(relevanceService, times(1)).getAllRelevancesByType("cost");
    }

    @Test
    void getAllRelevancesByType_UnknownType() throws Exception {
        when(relevanceService.getAllRelevancesByType("unknown")).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown relevance type: unknown"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/relevance/all?type=unknown")).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getAllRelevancesByTypeEmpty() throws Exception {
        when(relevanceService.getAllRelevancesByType("cost")).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/relevance/all?type=cost")).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    // --- /process endpoint tests ---
    @Test
    void getCostsByProcessId() throws Exception {
        Long processId = 10L;
        List<Cost> costs = List.of(createCost(1L, "Cost 1"));
        when(relevanceService.getRelevanceByTypeAndProcessId("cost", processId)).thenReturn((List) costs);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/relevance/process?type=cost&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(costs), result.getResponse().getContentAsString());
        verify(relevanceService, times(1)).getRelevanceByTypeAndProcessId("cost", processId);
    }

    @Test
    void getRelevanceByTypeAndProcessId_UnknownType() throws Exception {
        when(relevanceService.getRelevanceByTypeAndProcessId("unknown", 99L)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown relevance type: unknown"));
        Long processId = 99L;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/relevance/process?type=unknown&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getRelevanceByTypeAndProcessIdEmpty() throws Exception {
        Long processId = 99L;
        when(relevanceService.getRelevanceByTypeAndProcessId("cost", processId)).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/relevance/process?type=cost&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}
