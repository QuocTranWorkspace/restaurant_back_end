package com.example.restaurant.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.restaurant.model.BaseEntity;
import com.example.restaurant.model.PagerData;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

@Service
public abstract class BaseService<E extends BaseEntity> {

    private static final Logger log = LoggerFactory.getLogger(BaseService.class);

    @PersistenceContext
    private EntityManager entityManager;

    protected abstract Class<E> clazz();

    /**
     * Execute save or update the entity
     *
     * @param entity entity
     * @return entity
     */
    @Transactional
    public E saveOrUpdate(E entity) {
        if (entity.getId() == null || entity.getId() <= 0) {
            entity.setCreatedDate(new Date());
            entityManager.persist(entity); // Create
            return entity;
        } else {
            return entityManager.merge(entity); // Update
        }
    }

    /**
     * Soft delete the entity
     *
     * @param entity entity
     */
    @Transactional
    public void delete(E entity) {
        entity.setStatus(false);
        entityManager.merge(entity);
    }

    /**
     * Get record in the database with primary key
     *
     * @param primaryKey id
     * @return E entity
     */
    public E getById(int primaryKey) {
        return entityManager.find(clazz(), primaryKey);
    }

    /**
     * Get all the records in the database
     *
     * @return List of records
     */
    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        Table tbl = clazz().getAnnotation(Table.class);
        return entityManager.createNativeQuery("SELECT * FROM " + tbl.name(), clazz()).getResultList();
    }

    /**
     * Execute the query statement with paging data
     *
     * @param sql  query_string
     * @param page page
     * @return result
     */
    @SuppressWarnings("unchecked")
    public PagerData<E> getEntitiesByNativeSQL(String sql, int page) {
        if (page <= 0) {
            throw new IllegalAccessError("page must be greater or equal than 0");
        }

        PagerData<E> result = new PagerData<>();

        try {
            Query query = entityManager.createNativeQuery(sql, clazz());
            /*
             * Incase of pagination then the return result include page and current data
             */
            result.setCurrentPage(page);
            result.setTotalItems(query.getResultList().size());
            result.setSizeOfPage(8);

            query.setFirstResult((page - 1) * 8);
            query.setMaxResults(8);

            result.setData(query.getResultList());
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return result;
    }

    public E getEntityByNativeSQL(String sql) {
        try {
            return getEntitiesByNativeSQL(sql).get(0);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

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

}
