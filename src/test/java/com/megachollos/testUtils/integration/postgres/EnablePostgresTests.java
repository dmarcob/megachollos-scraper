package com.megachollos.testUtils.integration.postgres;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Target(TYPE)
@Retention(RUNTIME)
@ExtendWith(PostgresTestExtension.class)
public @interface EnablePostgresTests {

  /**
   * Specify the SQL script name to execute before all tests
   */
  String sqlScriptBeforeAll() default "";
}
