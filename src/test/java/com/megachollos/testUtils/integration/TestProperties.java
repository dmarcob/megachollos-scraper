package com.megachollos.testUtils.integration;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestProperties {

  // ----------------- ELASTIC PROPERTIES
  public static final String ELASTIC_SERVER_URL = "spring.elasticsearch.uris";

  public static void setElasticProperties(String url) {
    // Set server url. This property is required by Spring Data Elasticsearch autoconfiguration
    System.setProperty(ELASTIC_SERVER_URL, url);
  }

  // ----------------- POSTGRES PROPERTIES
  public static final String DATABASE_URL = "spring.datasource.url";
  public static final String DATABASE_USERNAME = "spring.datasource.username";
  public static final String DATABASE_PASSWORD = "spring.datasource.password";
  public static final String DATABASE_DRIVER = "spring.datasource.driver-class-name";
  public static final String DDL_AUTO = "spring.jpa.hibernate.ddl-auto";

  public static void setPostgresProperties(String jdbcUrl) {
    System.setProperty(DATABASE_URL, jdbcUrl);
    System.setProperty(DATABASE_USERNAME, "megachollos");
    System.setProperty(DATABASE_PASSWORD, "megachollos");
    System.setProperty(DATABASE_DRIVER, "org.postgresql.Driver");
    System.setProperty(DDL_AUTO, "update");
  }
}
