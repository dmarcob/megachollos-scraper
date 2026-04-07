package com.megachollos.testUtils.providers;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import java.util.Map;
import lombok.SneakyThrows;

public class ObjectProvider {

  /**
   * Returns an object mapper for serializing/deserializing objects
   */
  public static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return objectMapper;
  }

  @SneakyThrows
  public static Map<String, Object> fromClassPath(String path) {
    return getObjectMapper()
        .readValue(ObjectProvider.class.getClassLoader().getResourceAsStream(path), Map.class);
  }
}