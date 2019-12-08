package com.tl.commerce.service;

import com.tl.commerce.domain.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    int updateOrderState(String openid,String transactionId,String orderId);


    int delete(String orderId);

    int save(Order order);


    List<Map<String,Object>> getOrderByUserId(int userId);
    List<Order> getCurrentOrder(int userId,int goods_id,int specs_id);
    Order getOrderByOrderId(String orderId);

     String unifiedOrder(int goods_id,int specs_id,String userIp,int userId,String address,String consignee,String mobile) throws Exception;



}
