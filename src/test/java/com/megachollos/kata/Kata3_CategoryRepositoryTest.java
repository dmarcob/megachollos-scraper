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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class Kata3_CategoryRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private JpaCategoryRepository categoryRepository;

    //STEP 2
    @Test
    public void shouldSaveAndFindCategory(){
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .uniqueName("electronica")
                .displayName("Electronica")
                .build();

        categoryRepository.save(categoryEntity);

        var result = categoryRepository.findById("electronica");
        assertThat( result ).isPresent();
        assertThat ( result.get().getUniqueName() ).isEqualTo( "electronica");
        assertThat ( result.get().getDisplayName() ).isEqualTo( "Electronica");

    }

    //STEP 3
    @Test
    public void shouldSaveCategoryWithParent(){
        // 1- Create and save a parent category:
        CategoryEntity parentCategory = CategoryEntity.builder()
                .uniqueName("tech")
                .displayName("Technology")
                .build();
        categoryRepository.save(parentCategory);

        // 2- Create and save a child category:
        CategoryEntity childCategory = CategoryEntity.builder()
                .uniqueName("laptops")
                .displayName("displayName")
                .parentCategory( parentCategory )
                .parentCategoryUniqueName("tech")
                .build();
        categoryRepository.save(childCategory);

        var result = categoryRepository.findById("laptops");

        assertThat( result ).isPresent();
        assertThat( result.get().getParentCategory() ).isNotNull();
        assertThat( result.get().getParentCategory().getUniqueName() ).isEqualTo( "tech" );
    }

    //STEP 4
    @Test
    @Sql("/seed-categories.sql")
    public void shouldFindSeededCategories(){
        assertThat( categoryRepository.count() ).isEqualTo( 3 );

        var child1 = categoryRepository.findById("child1");
        assertThat( child1 ).isPresent();
        assertThat( child1.get().getParentCategory() ).isNotNull();
        assertThat( child1.get().getParentCategory().getUniqueName() ).isEqualTo("root1");
    }

    //STEP 5
    @Test
    @Sql("/seed-categories.sql")
    public void shouldFindRootCategories(){

        //Query categoryRepository.findAll() and filter categories where parent is null
            //The ideal would be to filter here, mandatory in main code but here in tests it's not as necessary
        //var result = categoryRepository.findAll();
            //Here it is, the ideal
        var result = categoryRepository.findByParentCategoryIsNull();


        //for ( CategoryEntity category : result)
        //    if(category.getParentCategory() != null) result.remove( category );
        //result.removeIf(category -> category.getParentCategory() != null);

        //Assert there are exactly 2 root categories
        assertThat( result.size() ).isEqualTo( 2 );
    }
}
