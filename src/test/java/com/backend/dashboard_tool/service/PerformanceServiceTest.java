package com.backend.dashboard_tool.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;

import com.backend.dashboard_tool.database.Strategy.KPIRepository;
import com.backend.dashboard_tool.database.Strategy.MitigationMeasureRepository;
import com.backend.dashboard_tool.database.Strategy.RiskRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.KPI;
import com.backend.dashboard_tool.entity.Strategy.MitigationMeasure;
import com.backend.dashboard_tool.entity.Strategy.Risk;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PerformanceServiceTest {
    @Mock
    private KPIRepository kpiRepository;

    @Mock
    private RiskRepository riskRepository;

    @Mock
    private MitigationMeasureRepository mitigationMeasureRepository;

    @Mock
    private ProcessService processService;

    @InjectMocks
    private PerformanceService performanceService;

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

    /**
     * Helper method to create a Risk object.
     * @param id
     * @param name
     * @return a Risk object with the specified parameters
     */
    private Risk createRisk(Long id, String name) {
        Risk risk = new Risk();
        risk.setId(id);
        risk.setName(name);
        return risk;
    }

    /**
     * Helper method to create a MitigationMeasure object.
     * @param id
     * @param name
     * @return a MitigationMeasure object with the specified parameters
     */
    private MitigationMeasure createMitigationMeasure(Long id, String name) {
        MitigationMeasure measure = new MitigationMeasure();
        measure.setId(id);
        measure.setName(name);
        return measure;
    }

    // --- /all endpoint tests ---

    @Test
    void getAllKPIs() throws Exception {
        List<KPI> kpis = List.of(createKPI(1L, "KPI 1"));
        when(kpiRepository.findAll()).thenReturn(kpis);

        List<?> result = performanceService.getAllPerformancesByType("kpi");
        assertEquals(kpis, result);
        verify(kpiRepository, times(1)).findAll();
    }

    @Test
    void getAllRisks() throws Exception {
        List<Risk> risks = List.of(createRisk(1L, "Risk 1"));
        when(riskRepository.findAll()).thenReturn(risks);

        List<?> result = performanceService.getAllPerformancesByType("risk");
        assertEquals(risks, result);
        verify(riskRepository, times(1)).findAll();
    }

    @Test
    void getAllMitigationMeasures() throws Exception {
        List<MitigationMeasure> measures = List.of(createMitigationMeasure(1L, "Measure 1"));
        when(mitigationMeasureRepository.findAll()).thenReturn(measures);

        List<?> result = performanceService.getAllPerformancesByType("mitigation-measure");
        assertEquals(measures, result);
        verify(mitigationMeasureRepository, times(1)).findAll();
    }

    @Test
    void getAllLayersByType_UnknownType() throws Exception {
        assertThrows(ResponseStatusException.class, () -> {
            performanceService.getAllPerformancesByType("unknown");
        });
    }

    // --- /process endpoint tests ---
    @Test
    void getKPIsByProcessId() throws Exception {
        Long processId = 10L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<KPI> kpis = List.of(createKPI(1L, "KPI 1"));
        when(kpiRepository.findByProcessId(processId)).thenReturn(kpis);

        List<?> result = performanceService.getPerformancesByTypeAndProcessId("kpi", processId);
        assertEquals(kpis, result);
        verify(kpiRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getRisksByProcessId() throws Exception {
        Long processId = 11L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<Risk> risks = List.of(createRisk(1L, "Risk 1"));
        when(riskRepository.findByProcessId(processId)).thenReturn(risks);

        List<?> result = performanceService.getPerformancesByTypeAndProcessId("risk", processId);
        assertEquals(risks, result);
        verify(riskRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getMitigationMeasuresByProcessId() throws Exception {
        Long processId = 12L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        List<MitigationMeasure> measures = List.of(createMitigationMeasure(1L, "Measure 1"));
        when(mitigationMeasureRepository.findByProcessId(processId)).thenReturn(measures);

        List<?> result = performanceService.getPerformancesByTypeAndProcessId("mitigation-measure", processId);
        assertEquals(measures, result);
        verify(mitigationMeasureRepository, times(1)).findByProcessId(processId);
    }

    @Test
    void getPerformancesByTypeAndProcessId_UnknownProcessId() throws Exception {
        Long processId = 99L;
        when(processService.getProcessById(processId)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            performanceService.getPerformancesByTypeAndProcessId("kpi", processId);
        });
    }

    @Test
    void getPerformancesByTypeAndProcessId_UnknownType() throws Exception {
        Long processId = 99L;
        when(processService.getProcessById(processId)).thenReturn(new ProcessEntity());

        assertThrows(ResponseStatusException.class, () -> {
            performanceService.getPerformancesByTypeAndProcessId("unknown", processId);
        });
    }
}
