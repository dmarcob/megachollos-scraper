package com.megachollos.testUtils.integration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public abstract class TestCallback implements BeforeAllCallback,
    BeforeEachCallback, AfterEachCallback, AfterAllCallback {

  @Override
  public abstract void afterAll(ExtensionContext extensionContext);

  @Override
  public abstract void afterEach(ExtensionContext extensionContext);

  @Override
  public abstract void beforeAll(ExtensionContext extensionContext);

  @Override
  public abstract void beforeEach(ExtensionContext extensionContext);

  /**
   * Gets with reflection the test class fields and its superclasses
   */
  protected List<Field> getAllFields(Class<?> testClass) {
    List<Field> fields = new LinkedList<>();
    while (testClass != null) {
      fields.addAll(Arrays.asList(testClass.getDeclaredFields()));
      testClass = testClass.getSuperclass();
    }

    return fields;
  }
}
