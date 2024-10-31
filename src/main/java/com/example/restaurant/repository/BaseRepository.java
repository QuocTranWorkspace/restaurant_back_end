package com.example.restaurant.repository;

import com.example.restaurant.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, Long> {
    // Common method to find by ID with Optional return type
    @SuppressWarnings("null")
    @NonNull
    @Override
    Optional<E> findById(Long id);

    // Common method to check if an entity exists by ID
    @SuppressWarnings("null")
    @Override
    boolean existsById(Long id);

    // find all entities
    @NonNull
    @Override
    List<E> findAll();
}