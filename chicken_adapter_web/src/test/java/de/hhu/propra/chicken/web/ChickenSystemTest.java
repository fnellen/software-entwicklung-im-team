package de.hhu.propra.chicken.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChickenSystemTest {

  @Autowired
  TestRestTemplate client;

  @Test
  @DisplayName("")
  void test_() {
    

  }

}
