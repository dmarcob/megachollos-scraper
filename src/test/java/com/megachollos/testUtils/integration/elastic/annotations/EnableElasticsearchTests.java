package com.megachollos.testUtils.integration.elastic.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.megachollos.testUtils.integration.elastic.ElasticsearchTestExtension;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Annotation to enable kafka tests.
 */
@Target(TYPE)
@Retention(RUNTIME)
@ExtendWith(ElasticsearchTestExtension.class)
public @interface EnableElasticsearchTests {

}