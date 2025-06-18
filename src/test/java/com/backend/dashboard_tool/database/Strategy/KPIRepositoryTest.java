package com.backend.dashboard_tool.database.Strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.KPI;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class KPIRepositoryTest {
    @Autowired
    private KPIRepository kpiRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding KPIs by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        KPI kpi = new KPI();
        kpi.setProcesses(List.of(process));
        kpiRepository.save(kpi);

        process.setKpis(List.of(kpi));
        processRepository.save(process);

        List<KPI> foundKpis = kpiRepository.findByProcessId(process.getId());
        assertThat(foundKpis).isNotEmpty();
        assertThat(foundKpis.get(0)).isEqualTo(kpi);
        assertThat(foundKpis.size()).isEqualTo(1);
    }

    /**
     * Test finding KPIs by process ID when no KPIs are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<KPI> foundKpis = kpiRepository.findByProcessId(process.getId());
        assertThat(foundKpis).isEmpty();
    }
}
