package com.example.springconditional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringConditionalApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    public static GenericContainer<?> devContainer = new GenericContainer<>("devapp")
            .withExposedPorts(8080);
    public static GenericContainer<?> prodContainer = new GenericContainer<>("prodapp")
            .withExposedPorts(8081);


    @BeforeAll
    public static void setUp() {
        devContainer.start();
        prodContainer.start();
    }

    @Test
    void devTest() {
        final Integer port = devContainer.getMappedPort(8080);
        final String expectedResult = "Current profile is dev";
        checkResult(port, expectedResult);
    }

    @Test
    void prodTest() {
        final Integer port = prodContainer.getMappedPort(8081);
        final String expectedResult = "Current profile is production";
        checkResult(port, expectedResult);
    }

    private void checkResult(Integer port, String expectedResult) {
        final ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/profile", String.class);
        Assertions.assertEquals(expectedResult, forEntity.getBody());
    }
}
