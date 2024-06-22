package com.akshit.treading.service;

import com.akshit.treading.domain.OrderType;
import com.akshit.treading.modal.Coin;
import com.akshit.treading.modal.Order;
import com.akshit.treading.modal.OrderItem;
import com.akshit.treading.modal.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);
    Order getOrderById(Long OrderId) throws Exception;

    List<Order> getAllOrderOfUser(Long userId,OrderType orderType,String assetSymbol);

    Order processOrder(Coin coin,double quantity,OrderType orderType,User user) throws Exception;
}
