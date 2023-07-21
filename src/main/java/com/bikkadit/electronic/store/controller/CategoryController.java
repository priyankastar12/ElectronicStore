package com.bikkadit.electronic.store.controller;

import com.bikkadit.electronic.store.dto.ApiResponseMessage;
import com.bikkadit.electronic.store.dto.CategoryDto;
import com.bikkadit.electronic.store.dto.ImageResponse;
import com.bikkadit.electronic.store.dto.PageableResponse;
import com.bikkadit.electronic.store.helper.AppConstant;
import com.bikkadit.electronic.store.service.CategoryService;
import com.bikkadit.electronic.store.service.FileService;
import org.hibernate.engine.jdbc.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.image.path}")
    private String imageUploadPath;

    private static Logger logger = LoggerFactory.getLogger(CategoryController.class);
    // create

    /**
     * @param categoryDto
     * @return
     * @author Priyanka_Pawar
     */
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        logger.info("Entering the request for the create Category data");

        // call service to call object
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        logger.info("Complete the request for the create Category data");

        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }
    // update

    /**
     * @param categoryId
     * @param categoryDto
     * @return
     * @athor Priyanka_Pawar
     */
    @PutMapping("/{categoryId}")
    private ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId, @RequestBody CategoryDto categoryDto) {
        logger.info("Entering the request for the updateCategory with categoryId :{}", categoryId);

        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        logger.info("Complete the request for the updateCategory with categoryId :{}", categoryId);

        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);

    }
    // delete

    /**
     * @param categoryId
     * @return
     * @author Priyanka_Pawar
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        logger.info("Entering the request for the deleteCategory with categoryId :{}", categoryId);

        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message(AppConstant.CATEGORY_DELETED).status(HttpStatus.OK).success(true).build();
        logger.info("Complete the request for the deleteCategory with categoryId :{}", categoryId);


        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }
    // get all

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @author Priyanka_Pawar
     */
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        logger.info("Entering the request for the getAll category data ");

        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Complete the request for the getAll product data ");

        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    // get single

    /**
     * @param categoryId
     * @return
     * @authr Priyanka_Pawar
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId) {
        logger.info("Entering the request for the getSingle  category data with categoryId : {} ", categoryId);

        CategoryDto categoryDto = categoryService.get(categoryId);
        logger.info("Complete the request for the getSingle  category data with categoryId : {} ", categoryId);

        return ResponseEntity.ok(categoryDto);


    }

    //upload Category image

    /**
     * @author Priyanka_Pawar
     * @param image
     * @param categoryId
     * @return
     * @throws IOException
     */
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestPart("categoryImage") MultipartFile image,
                                                             @PathVariable String categoryId) throws IOException {

        logger.info("Entering the request uploadCategoryImage method of CategoryController");
        String imageName = fileService.uploadFile(image, imageUploadPath);

        CategoryDto category = categoryService.get(categoryId);
        category.setCoverImage(imageName);

        CategoryDto categoryDto = categoryService.update(category, categoryId);

        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).message("Image Uploaded Successfully").build();
        logger.info("Completed the  uploadCategoryImage method");
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    /**
     * @author Priyanka_Pawar
     * @apiNote Serves Category Cover image
     */
    //serve image
    @GetMapping(value = "image/{categoryId}")
    public void serveCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {

        logger.info("Entering  serveCategoryImage method of CategoryController");
        CategoryDto category = categoryService.get(categoryId);
        logger.info("User Image:{}", category.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
        logger.info("Completed the  serveCategoryImage method");


    }
}