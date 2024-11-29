package com.rojin.flightstatus;

import java.util.List;

public class ApiResponse {
    private Pagination pagination;
    private List<Flight> data;

    // Getters and setters for pagination and data
    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Flight> getData() {
        return data;
    }

    public void setData(List<Flight> data) {
        this.data = data;
    }
}
