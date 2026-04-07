package com.megachollos.model.domain;

import static com.megachollos.model.domain.exceptions.errors.ModelError.MODEL_INVALID_DISPLAY_NAME;

import com.megachollos.brand.domain.Brand;
import com.megachollos.category.domain.Category;
import com.megachollos.shared.domain.exceptions.InternalException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@With
@Slf4j
public class Model {

  private Long id;
  private String displayName;
  private Category category;
  private Brand brand;

  public static String encode(String string) {
    if (StringUtils.isEmpty(string)) {
      return string;
    }

    try {
      return URLEncoder.encode(string, StandardCharsets.UTF_8);
    } catch (Exception e) {
      log.info("Error encoding string {}", string, e);
      throw new InternalException(MODEL_INVALID_DISPLAY_NAME, string);
    }
  }
}
