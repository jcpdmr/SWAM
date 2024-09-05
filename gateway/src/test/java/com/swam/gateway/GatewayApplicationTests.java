package com.swam.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest()
class GatewayApplicationTests {

    @Test
    void prova() {
        System.out.println("ok");
    }

}
