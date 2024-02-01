package com.lcwd.electronic.store.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private String categoryId;
    @NotBlank
    private String title;
    @NotBlank(message = "description required !!")
    private String description;
    private String coverImage;
}
