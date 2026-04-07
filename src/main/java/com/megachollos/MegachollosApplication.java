package com.megachollos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MegachollosApplication {

  public static void main(String[] args) {
    SpringApplication.run(MegachollosApplication.class, args);
  }

}
