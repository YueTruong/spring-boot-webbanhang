package com.vanlang.webbanhang.service;

import com.vanlang.webbanhang.model.CartItem;
import com.vanlang.webbanhang.model.Order;
import com.vanlang.webbanhang.model.OrderDetail;
import com.vanlang.webbanhang.repository.OrderDetailRepository;
import com.vanlang.webbanhang.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService;

    @Transactional
    public Order createOrder(String customerName, String customerAddress, String customerPhone, String customerEmail, String customerNote, String customerPayment, List<CartItem> cartItems) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setCustomerAddress(customerAddress);
        order.setCustomerPhone(customerPhone);
        order.setCustomerEmail(customerEmail);
        order.setCustomerNote(customerNote);
        order.setCustomerPayment(customerPayment);
        order = orderRepository.save(order);
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }
        cartService.clearCart();
        return order;
    }

//    public List<Order> getAllOrders() {
//        return orderRepository.findAll();
//    }

//    public Optional<Order> getOrderById(Long id) {
//        return orderRepository.findById(id);
//    }

    public List<Order> getAllOrdersWithDetails() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            List<OrderDetail> details = orderDetailRepository.findByOrder(order);
            order.setOrderDetails(details);
        }
        return orders;
    }
}
