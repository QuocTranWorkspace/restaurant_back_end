package com.example.Restaurant.model;

import java.util.List;

public class PagerData<E extends BaseEntity> {
    // Data list
    private List<E> data;

    // Total number of items
    private int totalItems;

    // Current page
    private int currentPage;

    // Number of items per page
    private int sizeOfPage;

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSizeOfPage() {
        return sizeOfPage;
    }

    public void setSizeOfPage(int sizeOfPage) {
        this.sizeOfPage = sizeOfPage;
    }
}
