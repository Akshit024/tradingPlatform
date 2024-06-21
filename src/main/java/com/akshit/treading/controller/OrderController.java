package com.akshit.treading.controller;

import com.akshit.treading.domain.OrderType;
import com.akshit.treading.modal.Coin;
import com.akshit.treading.modal.Order;
import com.akshit.treading.modal.User;
import com.akshit.treading.request.CreateOrderRequest;
import com.akshit.treading.service.CoinService;
import com.akshit.treading.service.OrderService;
import com.akshit.treading.service.UserService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;


    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwt, @RequestBody CreateOrderRequest req) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(req.getCoinId());
        Order order = orderService.processOrder(coin, req.getQuantity(),req.getOrderType(),user);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt,@PathVariable Long orderId) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);

        if(order.getUser().getId().equals(user.getId())){
            return new ResponseEntity<>(order,HttpStatus.OK);
        }else{
            throw new Exception("you don't have the excess of this Order");
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAllUserOrder(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false) String asset_symbol
    ) throws Exception {

        Long userId = userService.findUserProfileByJwt(jwt).getId();
        List<Order> list = orderService.getAllOrderOfUser(userId,order_type,asset_symbol);
        return new ResponseEntity<>(list,HttpStatus.OK);

    }





}
