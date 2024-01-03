package com.server.ptitFood.domain.services;

import com.server.ptitFood.domain.entities.OrderDetail;
import com.server.ptitFood.domain.entities.Product;
import com.server.ptitFood.domain.repositories.OrderDetailRepository;
import com.server.ptitFood.domain.repositories.OrderRepository;
import com.server.ptitFood.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final OrderDetailRepository orderDetailRepository;

    public DashboardService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            OrderDetailRepository orderDetailRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public Long countOrder() {
        return orderRepository.count();
    }

    public Long totalMoney() {
        return orderRepository.totalMoney();
    }

    public Long todayCount() {
        return orderRepository.todayCount();
    }

    public Long todayMoney() {
        return orderRepository.todayMoney();
    }

    // Best seller
    public List<OrderDetail> bestSeller() {
        List<OrderDetail> orderDetails = orderDetailRepository.bestSeller();
        System.out.println(orderDetails);
        return orderDetailRepository.bestSeller();
    }

    public Long totalProduct() {
        return productRepository.count();
    }

    public Long totalCategory() {
        return productRepository.count();
    }

    public List<Object[]> getMoneyByMonth() {
        return orderRepository.getMoneyByMonth2();
    }
}
