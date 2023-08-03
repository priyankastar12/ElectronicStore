package com.bikkadit.electronic.store.controller;

import com.bikkadit.electronic.store.dto.CategoryDto;
import com.bikkadit.electronic.store.dto.PageableResponse;
import com.bikkadit.electronic.store.entity.Category;
import com.bikkadit.electronic.store.entity.Product;
import com.bikkadit.electronic.store.service.CategoryService;
import com.bikkadit.electronic.store.service.FileService;
import com.bikkadit.electronic.store.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @Autowired
    private ModelMapper mapper;

    @MockBean
    private FileService fileService;

    Category category;
    Product product;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        category=Category.builder()
                .title("Mobiles")
                .coverImage("xyz.png")
                .description("this category is related to mobiles").build();

        product=Product.builder()
                .title("samsung")
                .description("related to mobile category")
                .category(category)
                .price(80000).discountedPrice(70000)
                .stock(true).live(true)
                .addedDate(new Date())
                .quantity(5)
                .productImage("www.png")
                .build();
    }
           // create category Test

    @Test
    public  void createCategoryTest() throws Exception {
        CategoryDto dto = mapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.create(Mockito.any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());

    }
    private String convertObjectToJsonString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;


        }
    }
               // update category

    public void updateCategoryTest() throws Exception {
        String categoryId="categoryTest";
        CategoryDto dto=this.mapper.map(category,CategoryDto.class);
        Mockito.when(categoryService.update(Mockito.any(),Mockito.anyString())).thenReturn(dto);
        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/categories/"+categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .exists());

    }
        // delete category
    @Test
    public void deleteCategoryTest() throws Exception {
         String categoryId="categoryTest";
         mockMvc.perform(MockMvcRequestBuilders.delete("/categories"+categoryId)
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk());

    }
    // get all category
    public void getAllCategoriesTest() throws Exception {
        CategoryDto category1=CategoryDto.builder()
                .title("mobiles")
                .coverImage("rrr.png")
                .description("this is category related mobile").build();

        CategoryDto category2=CategoryDto.builder()
                .title("TV")
                .coverImage("xyz.png")
                .description("this is category related tv").build();

        CategoryDto category3=CategoryDto.builder()
                .title("Cooller")
                .coverImage("wwk.png")
                .description("this is category related cooller").build();

        PageableResponse<CategoryDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setLastPage(false);
        pageableResponse.setPageSize(100);
        pageableResponse.setPageNumber(500);
        pageableResponse.setTotalElements(2000);
        Mockito.when(categoryService.getAll(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }
          // get single category Test
         @Test
         public void getSingleTest() throws Exception {
           String categoryId="categoryTest";
           CategoryDto category1= CategoryDto.builder()
                   .title("tv")
                   .coverImage("Tv.png")
                   .description("this tv related to category").build();

           Mockito.when(categoryService.get(Mockito.any())).thenReturn(category1);
           mockMvc.perform(MockMvcRequestBuilders.get("/Categories"+categoryId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
                   .andDo(print())
                   .andExpect(status().isOk());


         }

}
