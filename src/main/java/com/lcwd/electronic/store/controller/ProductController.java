package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.services.ProductService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        ProductDto createdProduct=productService.create(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto,@PathVariable String productId){
        ProductDto updatedProduct=productService.update(productDto,productId);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId){
        productService.delete(productId);
        ApiResponseMessage message=ApiResponseMessage.builder().message("Product deleted Successfully").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingle(@PathVariable String productId){
        ProductDto singleProduct = productService.getSingle(productId);
        return new ResponseEntity<>(singleProduct,HttpStatus.OK);

    }


    //get all

    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> gelAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> all = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(all,HttpStatus.OK);
    }

    //get all live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> gelAllLive(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> allLive = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allLive,HttpStatus.OK);
    }

    //search all
    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchAll(
            @PathVariable String query,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> all = productService.searchByTitle(query,pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(all,HttpStatus.OK);
    }

}
