package com.example.restaurant.service;

import com.example.restaurant.model.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * The type Base service.
 *
 * @param <E> the type parameter
 */
@Service
public abstract class BaseService<E extends BaseEntity> {
    private static final Logger log = LoggerFactory.getLogger(BaseService.class);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Clazz class.
     *
     * @return the class
     */
    protected abstract Class<E> clazz();

    /**
     * Save or update.
     *
     * @param entity the entity
     */
    @Transactional
    public void saveOrUpdate(E entity) {
        if (entity.getId() == null || entity.getId() <= 0) {
            entity.setCreatedDate(new Date());
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    /**
     * Delete.
     *
     * @param entity the entity
     */
    @Transactional
    public void delete(E entity) {
        entity.setStatus(false);
        entityManager.merge(entity);
    }

    /**
     * Gets by id.
     *
     * @param primaryKey the primary key
     * @return the by id
     */
    public E getById(int primaryKey) {
        return entityManager.find(clazz(), primaryKey);
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        Table tbl = clazz().getAnnotation(Table.class);
        return entityManager.createNativeQuery("SELECT * FROM " + tbl.name(), clazz()).getResultList();
    }

    /**
     * Gets entities by native sql.
     *
     * @param sql the sql
     * @return the entities by native sql
     */
    @SuppressWarnings("unchecked")
    public List<E> getEntitiesByNativeSQL(String sql) {
        List<E> result = new ArrayList<>();

        try {
            Query query = entityManager.createNativeQuery(sql, clazz());
            result = query.getResultList();
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return result;
    }

    /**
     * Update if not empty.
     *
     * @param fieldValue the field value
     * @param setter     the setter
     */
    public void updateIfNotEmpty(String fieldValue, Consumer<String> setter) {
        if (fieldValue != null && !fieldValue.isEmpty()) {
            setter.accept(fieldValue);
        }
    }
}
