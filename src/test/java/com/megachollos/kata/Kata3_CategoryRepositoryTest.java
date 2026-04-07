package com.megachollos.kata;

import com.megachollos.category.jpa.entities.CategoryEntity;
import com.megachollos.category.jpa.repositories.JpaCategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
public class Kata3_CategoryRepositoryTest {

    @Autowired
    JpaCategoryRepository jpaCategoryRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresA =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @Test
    void shouldSaveAndFindCategory() {
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .uniqueName("electronica")
                .displayName("Electronica")
                .build();

        jpaCategoryRepository.save(categoryEntity);

        Optional<CategoryEntity> categoryFound = jpaCategoryRepository.findById("electronica");

        assertTrue(categoryFound.isPresent());
        assertEquals("electronica", categoryFound.get().getUniqueName());
        assertEquals("Electronica", categoryFound.get().getDisplayName());
    }

    @Test
    void shouldSaveCategoryWithParent() {
        CategoryEntity parentEntity = CategoryEntity.builder()
                .uniqueName("tech")
                .displayName("Technology")
                .build();

        jpaCategoryRepository.save(parentEntity);

        CategoryEntity childEntity = CategoryEntity.builder()
                .uniqueName("laptops")
                .displayName("Laptops")
                .parentCategoryUniqueName("tech")
                .build();

        jpaCategoryRepository.save(childEntity);

        Optional<CategoryEntity> childFound = jpaCategoryRepository.findById("laptops");
        assertTrue(childFound.isPresent());
        assertEquals("tech", childFound.get().getParentCategoryUniqueName());
    }

    @Test
    @Sql("/seed-categories.sql")
    void shouldFindSeededCategories() {
        assertEquals(3, jpaCategoryRepository.count());

        Optional<CategoryEntity> categoryFound = jpaCategoryRepository.findById("child1");

        assertTrue(categoryFound.isPresent());
        assertEquals("root1", categoryFound.get().getParentCategoryUniqueName());
    }
}
