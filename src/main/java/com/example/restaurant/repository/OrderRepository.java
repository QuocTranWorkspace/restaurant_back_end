package com.example.restaurant.repository;

import com.example.restaurant.model.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Order repository.
 */
@Repository
public interface OrderRepository extends BaseRepository<OrderEntity> {
    /**
     * Find by code order entity.
     *
     * @param code the code
     * @return the order entity
     */
    OrderEntity findByCode(String code);

    /**
     * Find all by code list.
     *
     * @param code the code
     * @return the list
     */
    List<OrderEntity> findAllByCode(String code);
}