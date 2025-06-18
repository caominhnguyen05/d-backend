package com.backend.dashboard_tool.database.Strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.backend.dashboard_tool.database.ProcessRepository;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;
import com.backend.dashboard_tool.entity.Strategy.Product;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProcessRepository processRepository;

    /**
     * Test finding products by process ID.
     */
    @Test
    void findByProcessIdFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);

        Product product = new Product();
        product.setProcesses(List.of(process));
        productRepository.save(product);

        process.setProducts(List.of(product));
        processRepository.save(process);

        List<Product> foundProducts = productRepository.findByProcessId(process.getId());
        assertThat(foundProducts).isNotEmpty();
        assertThat(foundProducts.get(0)).isEqualTo(product);
        assertThat(foundProducts.size()).isEqualTo(1);
    }

    /**
     * Test finding products by process ID when no products are associated with the process.
     */
    @Test
    void findByProcessIdNotFound(){
        ProcessEntity process = new ProcessEntity();
        process.setType("Primary");
        process.setLevel(0);
        processRepository.save(process);

        List<Product> foundProducts = productRepository.findByProcessId(process.getId());
        assertThat(foundProducts).isEmpty();
    }
}
