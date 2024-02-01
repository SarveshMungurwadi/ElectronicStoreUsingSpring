package com.lcwd.electronic.store.controller;


import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String imageUploadPath;

    Logger logger= LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;
    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,@PathVariable String categoryId){
        CategoryDto update = categoryService.update(categoryDto, categoryId);

        return new ResponseEntity<>(update,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        categoryService.delete(categoryId);
        ApiResponseMessage categoryDeletedSuccesfully = ApiResponseMessage.builder().message("Category deleted succesfully").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(categoryDeletedSuccesfully,HttpStatus.OK);

    }

    //get all

    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }


    //get

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId){
        CategoryDto categoryDto = categoryService.get(categoryId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }

    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse>uploadUserImage(@PathVariable String categoryId, @RequestParam("categoryImage") MultipartFile image) throws IOException {
        String imageName=fileService.uploadFile(image,imageUploadPath);

        CategoryDto category=categoryService.get(categoryId);
        category.setCoverImage(imageName);
        CategoryDto categoryDto=categoryService.update(category,categoryId);
        ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }
    //serve user image
    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto category=categoryService.get(categoryId);
        logger.info("User image name : {}",category.getCoverImage());
        InputStream resource=fileService.getResources(imageUploadPath,category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
