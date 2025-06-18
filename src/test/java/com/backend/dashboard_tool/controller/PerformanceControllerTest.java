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

import com.backend.dashboard_tool.entity.Strategy.KPI;
import com.backend.dashboard_tool.service.PerformanceService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PerformanceController.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class PerformanceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PerformanceService performanceService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Helper method to create a KPI object.
     * @param id
     * @param name
     * @return a KPI object with the specified parameters
     */
    private KPI createKPI(Long id, String name) {
        KPI kpi = new KPI();
        kpi.setId(id);
        kpi.setName(name);
        return kpi;
    }

    // --- /all endpoint tests ---

    @Test
    void getAllKPIs() throws Exception {
        List<KPI> kpis = List.of(createKPI(1L, "KPI 1"));
        when(performanceService.getAllPerformancesByType("kpi")).thenReturn((List) kpis);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/performance/all?type=kpi")).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(kpis), result.getResponse().getContentAsString());
        verify(performanceService, times(1)).getAllPerformancesByType("kpi");
    }

    @Test
    void getAllLayersByType_UnknownType() throws Exception {
        when(performanceService.getAllPerformancesByType("unknown")).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown Performance type: unknown"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/performance/all?type=unknown")).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getAllLayersByTypeEmpty() throws Exception {
        when(performanceService.getAllPerformancesByType("kpi")).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/performance/all?type=kpi")).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    // --- /process endpoint tests ---
    @Test
    void getKPIsByProcessId() throws Exception {
        Long processId = 10L;
        List<KPI> kpis = List.of(createKPI(1L, "KPI 1"));
        when(performanceService.getPerformancesByTypeAndProcessId("kpi", processId)).thenReturn((List) kpis);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/performance/process?type=kpi&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(kpis), result.getResponse().getContentAsString());
        verify(performanceService, times(1)).getPerformancesByTypeAndProcessId("kpi", processId);
    }

    @Test
    void getLayerByTypeAndProcessId_UnknownType() throws Exception {
        when(performanceService.getPerformancesByTypeAndProcessId("unknown", 99L)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown Performance type: unknown"));
        Long processId = 99L;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/performance/process?type=unknown&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void getLayerByTypeAndProcessIdEmpty() throws Exception {
        Long processId = 99L;
        when(performanceService.getPerformancesByTypeAndProcessId("kpi", processId)).thenReturn(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/performance/process?type=kpi&processId=" + processId)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}
