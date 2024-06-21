package com.akshit.treading.service.Implementation;

import com.akshit.treading.domain.OrderStatus;
import com.akshit.treading.domain.OrderType;
import com.akshit.treading.modal.Coin;
import com.akshit.treading.modal.Order;
import com.akshit.treading.modal.OrderItem;
import com.akshit.treading.modal.User;
import com.akshit.treading.repository.OrderItemRepository;
import com.akshit.treading.repository.OrderRepository;
import com.akshit.treading.service.OrderService;
import com.akshit.treading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private WalletService walletService;

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        Order order = new Order();
        double price = orderItem.getQuantity()*orderItem.getCoin().getCurrentPrice();
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setPrice(BigDecimal.valueOf(price));
        order.setUser(user);
        order.setTimeStamp(LocalDateTime.now());
        order.setOrderstatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long OrderId) throws Exception {
        Optional<Order> order = orderRepository.findById(OrderId);
        if(order.isEmpty()){
            throw new Exception("Order Not Exist");
        }
        return order.get();
    }

    private  OrderItem createOrderItem(Coin coin,double quantity,double buyPrice,double sellPrice){
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);

        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coin coin,double quantity,User user) throws Exception {
        if(quantity<=0) throw new Exception("quantity should be grater then zero");
        double buyPrice = coin.getCurrentPrice();
        OrderItem item = createOrderItem(coin,quantity,buyPrice,0);
        Order order = createOrder(user,item,OrderType.BUY);
        item.setOrder(order);
        walletService.payOrderPayment(order,user);
        order.setOrderstatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepository.save(order);
        // create Asset
        return savedOrder;
    }

    @Transactional
    public Order sellAsset(Coin coin,double quantity,User user) throws Exception {
        if(quantity<=0) throw new Exception("quantity should be grater then zero");
        double sellPrice = coin.getCurrentPrice();
        OrderItem item = createOrderItem(coin,quantity,0,sellPrice);
        Order order = createOrder(user,item,OrderType.SELL);
        item.setOrder(order);
        walletService.payOrderPayment(order,user);
        order.setOrderstatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.SELL);
        Order savedOrder = orderRepository.save(order);
        // create Asset
        return savedOrder;
    }
    @Override
    public List<Order> getAllOrderOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return orderRepository.findBYUserId(userId);
    }

    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {
        if(orderType.equals(OrderType.BUY)){
            return buyAsset(coin,quantity,user);
        }else if(orderType.equals(OrderType.SELL)){
            return sellAsset(coin,quantity,user);
        }
        throw new Exception("Invalid Order Type");
    }
}
