package com.example.restaurant.model;

import java.util.List;

/**
 * The type Pager data.
 *
 * @param <E> the type parameter
 */
public class PagerData<E extends BaseEntity> {
    // Data list
    private List<E> data;

    // Total number of items
    private int totalItems;

    // Current page
    private int currentPage;

    // Number of items per page
    private int sizeOfPage;

    /**
     * Gets data.
     *
     * @return the data
     */
    public List<E> getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(List<E> data) {
        this.data = data;
    }

    /**
     * Gets total items.
     *
     * @return the total items
     */
    public int getTotalItems() {
        return totalItems;
    }

    /**
     * Sets total items.
     *
     * @param totalItems the total items
     */
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    /**
     * Gets current page.
     *
     * @return the current page
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets current page.
     *
     * @param currentPage the current page
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Gets size of page.
     *
     * @return the size of page
     */
    public int getSizeOfPage() {
        return sizeOfPage;
    }

    /**
     * Sets size of page.
     *
     * @param sizeOfPage the size of page
     */
    public void setSizeOfPage(int sizeOfPage) {
        this.sizeOfPage = sizeOfPage;
    }
}