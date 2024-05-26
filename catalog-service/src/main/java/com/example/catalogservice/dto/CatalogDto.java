package com.example.catalogservice.dto;

import lombok.Data;

@Data
public class CatalogDto {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId; //uuid 일듯
    private String userId; //uuid
}
