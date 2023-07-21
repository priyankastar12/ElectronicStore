package com.bikkadit.electronic.store.controller;


import com.bikkadit.electronic.store.dto.ApiResponseMessage;
import com.bikkadit.electronic.store.dto.ImageResponse;
import com.bikkadit.electronic.store.dto.PageableResponse;
import com.bikkadit.electronic.store.dto.ProductDto;
import com.bikkadit.electronic.store.helper.AppConstant;
import com.bikkadit.electronic.store.service.FileService;
import com.bikkadit.electronic.store.service.ProductService;
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
@RequestMapping("/products")

public class ProductController {
    @Autowired
    private ProductService productService;

    @Value("${product.image.path}")
    private String imagePath;

    @Autowired
    private FileService fileService;

    private static Logger logger = LoggerFactory.getLogger(ProductController.class);

    // create

    /**
     * @author Priyanka_Pawar
     * @param productDto
     * @return
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        logger.info("Entering the request for the create product data");
        ProductDto createdProduct = productService.create(productDto);
        logger.info("Complete the request for the create product data");

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // update

    /**
     * @author Priyanka_Pawar
     * @param productId
     * @param productDto
     * @return
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId, @RequestBody ProductDto productDto) {
        logger.info("Entering the request for the update data with productId :{}",productId);

        ProductDto updatedProduct = productService.update(productDto, productId);
        logger.info("Complete the request for the update data with productId :{}" , productId);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);


    }

    // delete

    /**
     * @author Priyanka_Pawar
     * @param productId
     * @return
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId) {
        logger.info("Entering the request for the delete data with productId :{}",productId);

        productService.delete(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message(AppConstant.PRODUCT_DELETED).status(HttpStatus.OK).success(true).build();
        logger.info("Complete the request for the delete data with productId :{}",productId);

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    // get single

    /**
     * @authr Priyanka_Pawar
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
        logger.info("Entering the request for the getProduct data with productId :{}",productId);

        ProductDto productDto = productService.get(productId);
        logger.info("Complete the request for the getProduct data with productId :{}",productId);

        return new ResponseEntity<>(productDto, HttpStatus.OK);


    }

    // get all

    /**
     * @author Priyanka_Pawar
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        logger.info("Entering the request for the getAll product data ");

        PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Complete the request for the getAll product data");

        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }
    // get all live

    /**
     * @author Priyanka_Pawar
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */



    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        logger.info("Entering the request for the getAllLive product data ");

        PageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Complete the request for the getAllLive data ");

        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    // search all

    /**
     * @author Priyanka_Pawar
     * @param query
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */


    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
            @PathVariable String query,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        logger.info("Entering the request for the searchProduct data");

        PageableResponse<ProductDto> pageableResponse = productService.searchByTitle(query,   pageNumber, pageSize, sortBy, sortDir);
        logger.info("Complete the request for the searchProduct data");
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //upload image

    /**
     * @author Priyanka_Pawar
     * @apiNote Uploads Product image
     */
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(@PathVariable String productId,
                                                     @RequestParam("productImage") MultipartFile image) throws IOException {
        logger.info("Enitializing uploadImage method of ProductController");
        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto productDto = productService.get(productId);
        productDto.setProductImage(fileName);
        ProductDto updatedProduct = productService.update(productDto, productId);
        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImage()).message("Product Image is Successfully Uploaded !!").status(HttpStatus.CREATED).success(true).build();
        logger.info("Completed of method uploadProductImage");
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    //serve image

    /**
     * @author Priyanka_Pawar
     * @apiNote Serves User image
     */
    @GetMapping(value = "image/{productId}")
    public  void serveProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

        logger.info("Enitializing ServeProductImage method of ProductController");
        ProductDto productDto = productService.get(productId);
        logger.info("Product Image:{}",productDto.getProductImage());
        InputStream resource = fileService.getResource(imagePath, productDto.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        logger.info("Completed the method of serveProductImage");


    }


}