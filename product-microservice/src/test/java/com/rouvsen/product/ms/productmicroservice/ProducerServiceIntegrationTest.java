package com.rouvsen.product.ms.productmicroservice;

import com.rouvsen.product.ms.productmicroservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.rocksdb.util.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test") //application-test.yml
@EmbeddedKafka(partitions = 3, count = 3, controlledShutdown = true)
@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class ProducerServiceIntegrationTest {

    @MockitoBean
    private ProductService productService;

    @Mock
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Mock
    private Environment environment;

    @Test
    void testCreateProduct_whenGivenValidProductDetails_successfullySendsKafkaMessage() {

        //Arrange

        //Act

        //Assert

    }

}
