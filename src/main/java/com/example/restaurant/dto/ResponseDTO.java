package com.example.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Response dto.
 */
@Setter
@Getter
public class ResponseDTO {
    private int status;
    private String message;
    private Object data;

    /**
     * Instantiates a new Response dto.
     *
     * @param status  the status
     * @param message the message
     * @param data    the data
     */
    public ResponseDTO(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
