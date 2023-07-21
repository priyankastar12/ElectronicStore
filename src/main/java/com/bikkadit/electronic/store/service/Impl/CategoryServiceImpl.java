package com.bikkadit.electronic.store.service.Impl;

import com.bikkadit.electronic.store.dto.CategoryDto;
import com.bikkadit.electronic.store.dto.PageableResponse;
import com.bikkadit.electronic.store.entity.Category;
import com.bikkadit.electronic.store.exception.ResourceNotFoundException;
import com.bikkadit.electronic.store.helper.AppConstant;
import com.bikkadit.electronic.store.helper.Helper;
import com.bikkadit.electronic.store.repository.CategoryRepository;
import com.bikkadit.electronic.store.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
     @Autowired
     private CategoryRepository categoryRepository;
     @Autowired
     private ModelMapper mapper;



    private static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        logger.info("initiating the service call create category data");

        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category   = mapper.map(categoryDto, Category.class);
     Category savedCategory= categoryRepository.save(category);
        logger.info("Complete the service call create category data");

        return mapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        logger.info("initiating the service call update category data with categoryId :{}",categoryId);

        // get category of given id
    Category category=  categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND));
       // update category details
     category.setTitle(categoryDto.getTitle());
     category.setDescription(categoryDto.getDescription());
     category.setCoverImage(categoryDto.getCoverImage());
     Category updatedCategory= categoryRepository.save(category);
        logger.info("complete the service call update category data with categoryId :{}",categoryId);

        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        logger.info("initiating the service call delete the data with categoryId :{}" , categoryId);
        // get category of given id
        Category category=  categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);



        logger.info("Complete the service call delete the data with categoryId :{}" , categoryId);

    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        logger.info("initiating the service call getAll  category data  ");

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()): (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
    Page<Category> page=  categoryRepository.findAll(pageable);
    PageableResponse<CategoryDto> pageableResponse= Helper.getPageableResponse(page,CategoryDto.class);
        logger.info("Complete the service call getAll the category  data  ");

        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {
        logger.info("initiating the service call  get  category data with Id :{}",categoryId);

        Category category=  categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND));
        logger.info("Complete the service call  get  category data with Id :{}",categoryId);

        return mapper.map(category,CategoryDto.class);
    }
}
