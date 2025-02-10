package com.springproject.ecommercecore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // 📌 Indica que debe usar application-test.yml
class EcommerceCoreApplicationTests {

    @Test
    void contextLoads() {
    }

}
