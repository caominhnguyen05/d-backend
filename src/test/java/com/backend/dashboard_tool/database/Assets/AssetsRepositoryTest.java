package com.backend.dashboard_tool.database.Assets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;

import com.backend.dashboard_tool.entity.Assets_Facilities.Asset;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AssetsRepositoryTest {
    @Autowired
    private AssetRepository assetsRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding assets by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        Asset asset = new Asset();
        asset.setProcesses(List.of(process));
        assetsRepository.save(asset);

        process.setAssets(List.of(asset));
        processRepository.save(process);

        List<Asset> foundAssets = assetsRepository.findByProcessId(process.getId());
        assertThat(foundAssets).isNotEmpty();
        assertThat(foundAssets.get(0)).isEqualTo(asset);
        assertThat(foundAssets.size()).isEqualTo(1);
    }

    /**
     * Test finding assets by process ID when no assets are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Asset> foundAssets = assetsRepository.findByProcessId(process.getId());
        assertThat(foundAssets).isEmpty();
    }
}
