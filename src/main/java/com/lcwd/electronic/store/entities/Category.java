package com.lcwd.electronic.store.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "id")
    private String categoryId;
    @Column(name = "category_title",length = 60)
    private String title;
    @Column(name = "category_desc",length = 500)

    private String description;
    private String coverImage;
}
