package com.megachollos.model.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ModelTest extends BaseUnitTest {

  public static Stream<Arguments> encodeArgumentProvider() {
    return Stream.of(
        Arguments.of("", ""),
        Arguments.of("Samsung Galaxy", "Samsung+Galaxy")
    );
  }


  @ParameterizedTest
  @MethodSource("encodeArgumentProvider")
  void encode_OK(String given, String expected) {
    // WHEN
    String result = Model.encode(given);

    // THEN
    assertThat(result).isEqualTo(expected);
  }
}
