package com.bikkadit.electronic.store.service;

import com.bikkadit.electronic.store.dto.CategoryDto;
import com.bikkadit.electronic.store.dto.PageableResponse;
import com.bikkadit.electronic.store.dto.UserDto;
import com.bikkadit.electronic.store.entity.Category;
import com.bikkadit.electronic.store.repository.CategoryRepository;
import com.bikkadit.electronic.store.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CategoryServiceTest {
    @MockBean
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    private ModelMapper mapper;

    Category category;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init(){
        category=Category.builder()
                .title("Mobiles")
                .description("it is most expensive mobile")
                .coverImage("abc.png").build();
    }
       // update category test

    @Test
    public void createCategoryTest() {
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        Category category1 = categoryRepository.save(category);
        System.out.println(category1.getTitle());
        Assertions.assertNotNull(category1);
        Assertions.assertEquals("Mobiles", category1.getTitle());

    }

    @Test
    public void updateCategoryTest() {
        String categoryId = "categoryIdTest";


        CategoryDto categoryDto = CategoryDto.builder()
                .title("Mobiles and Accesories")
                .coverImage("xyz.png")
                .description("this category is related to mobiles").build();
        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        System.out.println(updatedCategory.getTitle());
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(categoryDto.getTitle(), updatedCategory.getTitle());
    }
       // delete category
    @Test
    public void deleteCategoryTest(){
        String categoryId="categoryIdTest";
        Mockito.when(categoryRepository.findById("categoryIdTest")).thenReturn(Optional.of(category));
        categoryService.delete(categoryId);
        Mockito.verify(categoryRepository,Mockito.times(1)).delete(category);
    }
      // get all category
    public void getAllCategoryTest(){
        Category category1=Category.builder()
                .title("camera")
                .coverImage("abc.png")
                .description("this camera have good clarity").build();

        Category category2=Category.builder()
                .title("TV")
                .coverImage("xyz.png")
                .description("this Tv have good clarity").build();


        List<Category> categoryList= Arrays.asList(category,category1,category2);
        Page<Category> page= new PageImpl<>(categoryList);
        Mockito.when(categoryRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<CategoryDto> all=categoryService.getAll(1,2,"title","asc");
        Assertions.assertEquals(3,all.getContent().size());




    }
    // get category
    @Test
    public void getCategoryTest(){
        String categoryId="categoryIdTest";
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        // actual call of service method
        CategoryDto categoryDto=categoryService.get(categoryId);
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(categoryDto.getTitle(),category.getTitle(),"title not matched");
    }


    }




