package com.target.myretail;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.target.myretail.config.CassandraConfig;
import com.target.myretail.service.model.ProductInfo;
import com.target.myretail.service.model.ProductPrice;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ProductApplication.class)
@ContextConfiguration(classes = {CassandraConfig.class})
@ComponentScan(basePackages = {
        "com.target.myretail"
})
@TestExecutionListeners(listeners = {
        CassandraUnitDependencyInjectionTestExecutionListener.class,
        CassandraUnitTestExecutionListener.class,
        ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
@EmbeddedCassandra(timeout = 60000)
@CassandraDataSet(value = {"bootstrap_test.cql"}, keyspace = "myretail")
@AutoConfigureWebTestClient
public class ProductApplicationIntTest {

    @Autowired
    private CassandraAdminOperations adminTemplate;

    @Autowired
    private WebTestClient webTestClient;

    private static final String DATA_TABLE_NAME = "product_price";
    private static final String PRODUCT_URL = "/retail/product/";



    @BeforeClass
    public static void startCassandraEmbedded()
            throws ConfigurationException, TTransportException, IOException, InterruptedException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        Thread.sleep(5000);
    }

    @AfterClass
    public static void stopCassandraEmbedded() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }

    @Test
    public void get_happyPath() {
        webTestClient.get()
                     .uri(PRODUCT_URL + "13860428")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectHeader()
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .expectBody()
                     .jsonPath("$.id")
                     .isEqualTo("13860428")
                     .jsonPath("$.name")
                     .isEqualTo("The Big Lebowski (Blu-ray)")
                     .jsonPath("$.current_price.value")
                     .isEqualTo("299.99")
                     .jsonPath("$.current_price.currency_code")
                     .isEqualTo("USD");
    }

    @Test
    public void get_failure_not_found_db() {
        webTestClient.get()
                     .uri(PRODUCT_URL + "13860429")
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectHeader()
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .expectBody()
                     .jsonPath("$.message")
                     .isEqualTo("Product Not Found");
    }

    @Test
    public void get_failure_data_not_found_redsky() {
        webTestClient.get()
                     .uri(PRODUCT_URL + "13860500")
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectHeader()
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .expectBody()
                     .jsonPath("$.message")
                     .isEqualTo("Product Not Found");
    }

    @Test
    public void put_success() {

        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId(13860428L);
        productInfo.setName("The Big Lebowski (Blu-ray)");
        productInfo.setProductPrice(new ProductPrice(BigDecimal.valueOf(100.99),"USD"));

        webTestClient.put()
                     .uri(PRODUCT_URL + "13860428")
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .accept(MediaType.APPLICATION_JSON_UTF8)
                     .body(Mono.just(productInfo), ProductInfo.class)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectHeader()
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .expectBody()
                     .jsonPath("$.id")
                     .isEqualTo("13860428")
                     .jsonPath("$.name")
                     .isEqualTo("The Big Lebowski (Blu-ray)")
                     .jsonPath("$.current_price.value")
                     .isEqualTo("100.99")
                     .jsonPath("$.current_price.currency_code")
                     .isEqualTo("USD");
    }

    @Test
    public void put_failure_not_found_db() {

        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId(13860429L);
        productInfo.setName("The Big Lebowski (Blu-ray)");
        productInfo.setProductPrice(new ProductPrice(BigDecimal.valueOf(100.99),"USD"));

        webTestClient.put()
                     .uri(PRODUCT_URL + "13860429")
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .accept(MediaType.APPLICATION_JSON_UTF8)
                     .body(Mono.just(productInfo), ProductInfo.class)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectHeader()
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .expectBody()
                     .jsonPath("$.message")
                     .isEqualTo("Product Not Found");
    }

    @Test
    public void put_failure_data_not_match() {

        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId(13860429L);
        productInfo.setName("The Big Lebowski (Blu-ray)");
        productInfo.setProductPrice(new ProductPrice(BigDecimal.valueOf(100.99),"USD"));

        webTestClient.put()
                     .uri(PRODUCT_URL + "3860500")
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .accept(MediaType.APPLICATION_JSON_UTF8)
                     .body(Mono.just(productInfo), ProductInfo.class)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectHeader()
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .expectBody()
                     .jsonPath("$.message")
                     .isEqualTo("Product ID does not match with Product");
    }

    @Test
    public void put_failure_field_errors() {

        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId(3860500L);
        productInfo.setName("The Big Lebowski (Blu-ray)");
        productInfo.setProductPrice(new ProductPrice(BigDecimal.valueOf(100.99),"USDD"));

        webTestClient.put()
                     .uri(PRODUCT_URL + "3860500")
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .accept(MediaType.APPLICATION_JSON_UTF8)
                     .body(Mono.just(productInfo), ProductInfo.class)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectHeader()
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .expectBody()
                     .jsonPath("$.message")
                     .isEqualTo("Validation failed for object='productInfo'. Error count: 1");
    }
}
