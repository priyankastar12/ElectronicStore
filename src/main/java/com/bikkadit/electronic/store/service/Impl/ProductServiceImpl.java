package com.bikkadit.electronic.store.service.Impl;

import com.bikkadit.electronic.store.dto.PageableResponse;
import com.bikkadit.electronic.store.dto.ProductDto;
import com.bikkadit.electronic.store.entity.Product;
import com.bikkadit.electronic.store.exception.ResourceNotFoundException;
import com.bikkadit.electronic.store.helper.AppConstant;
import com.bikkadit.electronic.store.helper.Helper;
import com.bikkadit.electronic.store.repository.ProductRepository;
import com.bikkadit.electronic.store.service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;

    private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);


    @Override
    public ProductDto create(ProductDto productDto) {
        logger.info("initiating the service call create Product data");

        Product product=  mapper.map(productDto, Product.class);
        // product id
        String productId= UUID.randomUUID().toString();
        product.setProductId(productId);
        // added
        product.setAddedDate(new Date());

    Product saveProduct=  productRepository.save(product);
        logger.info("Complete the service call create Product data");

        return mapper.map(saveProduct,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        logger.info("initiating the service call update  Product    data with productId :{}",productId);

        // fetch the product of given id
    Product product= productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT_NOT_FOUND));
           product.setTitle(productDto.getTitle());
           product.setDescription(productDto.getDescription());
           product.setPrice(productDto.getPrice());
           product.setDiscountedPrice(productDto.getDiscountedPrice());
           product.setQuantity(productDto.getQuantity());
           product.setLive(productDto.isLive());
           product.setStock(productDto.isStock());

        // save the entity
          Product updatedProduct= productRepository.save(product);
        logger.info("Complete the service call update User data with productId :{}",productId);

        return mapper.map(updatedProduct,ProductDto.class);
    }



    @Override
    public void delete(String productId) {
        logger.info("initiating the service call delete Product data");

        Product product= productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT_NOT_FOUND));
          productRepository.delete(product);

        logger.info("Complete the service call delete Product data");



    }

    @Override
    public ProductDto get(String productId) {
        logger.info("initiating the service call get Product data by using singleId with productId : {}" ,productId);

        Product product= productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT_NOT_FOUND));
        logger.info("Complete the service call get Product data by using singleId with productId : {}" ,productId);

        return mapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto>getAll(int pageNumber,int pageSize, String sortBy,String sortDir) {

        logger.info("initiating the service call getAll Product data  ");

        Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page= productRepository.findAll(pageable);
        logger.info("Complete the service call getAll Product data  ");

        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize, String sortBy,String sortDir) {
        logger.info("initiating the service call getAllLive Product data ");


        Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
                Page<Product> page= productRepository.findByLiveTrue(pageable);
        logger.info("Complete the service call getAllLive Product data ");

        return Helper.getPageableResponse(page,ProductDto.class);



    }

    @Override
    public PageableResponse<ProductDto>searchByTitle(String subTitle,int pageNumber,int pageSize, String sortBy,String sortDir) {
        logger.info("initiating the service call search product data   with subTitle : {} ",subTitle);

        Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page= productRepository.findByTitleContaining(subTitle,pageable);
        logger.info("Complete the service call search product data  with subTitle : {} ",subTitle);

        return Helper.getPageableResponse(page,ProductDto.class);


    }
}
