package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;

public interface CategoryService {

    //create
    CategoryDto create(CategoryDto categoryDto);
    //update
    CategoryDto update(CategoryDto categoryDto,String categoryId);
    //delete
    void delete(String categoryId);
    //get all


    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
    CategoryDto get(String categoryId);

    //Search by title
    CategoryDto getUserByTitle(String title);
}
