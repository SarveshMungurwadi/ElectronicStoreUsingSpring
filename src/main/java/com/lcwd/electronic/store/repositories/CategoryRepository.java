package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,String> {
    Optional<Category> findByTitle(String email);
}
