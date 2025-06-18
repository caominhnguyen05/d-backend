package com.backend.dashboard_tool.database.Strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.Goal;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class GoalRepositoryTest {
    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding goals by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        Goal goal = new Goal();
        goal.setProcesses(List.of(process));
        goalRepository.save(goal);

        process.setGoals(List.of(goal));
        processRepository.save(process);

        List<Goal> foundGoals = goalRepository.findByProcessId(process.getId());
        assertThat(foundGoals).isNotEmpty();
        assertThat(foundGoals.get(0)).isEqualTo(goal);
        assertThat(foundGoals.size()).isEqualTo(1);
    }

    /**
     * Test finding goals by process ID when no goals are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Goal> foundGoals = goalRepository.findByProcessId(process.getId());
        assertThat(foundGoals).isEmpty();
    }
}
