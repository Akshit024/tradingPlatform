package com.akshit.treading.request;

import com.akshit.treading.domain.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {
    private String coinId;
    private Double quantity;
    private OrderType orderType;

}
