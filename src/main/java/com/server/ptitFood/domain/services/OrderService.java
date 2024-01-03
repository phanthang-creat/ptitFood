package com.server.ptitFood.domain.services;

import com.server.ptitFood.domain.dto.OrderDto;
import com.server.ptitFood.domain.entities.*;
import com.server.ptitFood.domain.repositories.OrderDetailRepository;
import com.server.ptitFood.domain.repositories.OrderRepository;
import com.server.ptitFood.domain.repositories.OrderStatusRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final ProductService productService;

    private final DiscountService discountService;

    private final UserService userService;

    private final OrderStatusRepository orderStatusRepository;

    private final AdminControlService adminControlService;

    private final CartService cartService;

    public OrderService(
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository, ProductService productService,
            DiscountService discountService,
            UserService userService,
            OrderStatusRepository orderStatusRepository, AdminControlService adminControlService,
            CartService cartService
    ) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
        this.discountService = discountService;
        this.userService = userService;
        this.orderStatusRepository = orderStatusRepository;
        this.adminControlService = adminControlService;
        this.cartService = cartService;
    }

    @Transactional
    public boolean createOrder(
            OrderDto orderDto
    ) {

        Customer user = userService.getCurrentUser();

        List<Cart> carts = cartService.getCart();

        List<Product> currentProductsInRepository = new ArrayList<>();

        int totalMoney = 0;

        for (Cart cart : carts) {
            currentProductsInRepository.add(cart.getProduct());
            totalMoney += cart.getProduct().getPriceSale() * cart.getQuantity();
        }


        for (Product product : currentProductsInRepository) {
            if (product.getNumberBuy() < carts.get(currentProductsInRepository.indexOf(product)).getQuantity())
                return false;
        }

        Discount discount = null;

        if (orderDto.getCodeDiscount() != null) {
            discount = discountService.getDiscountByCode(orderDto.getCodeDiscount());
            if (discount == null) {
                return false;
            }

            if (discount.getDiscount() > totalMoney) {
                orderDto.setMoney(0);
            } else {
                orderDto.setMoney(totalMoney - discount.getDiscount());
            }
        }

        for (Product product : currentProductsInRepository) {
            int number = carts.get(currentProductsInRepository.indexOf(product)).getQuantity();
            productService.updateNumberAndNumberBuy(product.getId(), number);
        }

        OrderStatus orderStatus = orderStatusRepository.getOrderStatusById(1);

        Order order = new Order();
        order.setFullname(orderDto.getFullname());
        order.setPhone(orderDto.getPhone());
        order.setAddress(orderDto.getAddress());
        order.setMoney(totalMoney);
        order.setDiscount(discount);
        order.setOrderStatus(orderStatus);
        order.setUser(user);
        order.setOrderDate(new java.sql.Date(System.currentTimeMillis()));

        Order newOrder = orderRepository.save(order);

        for (Product product : currentProductsInRepository) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setCount(carts.get(currentProductsInRepository.indexOf(product)).getQuantity());
            orderDetail.setPrice(product.getPriceSale());

            orderDetailRepository.save(orderDetail);
        }

        cartService.deleteAllCart();

        return true;
    }

    public List<Order> getAllOrder() {
        Customer user = userService.getCurrentUser();

        return orderRepository.findAllByUser(user);
    }

    @Transactional
    public Order getDecryptionById(Integer id) {
        Customer user = userService.getCurrentUser();

        Order order = orderRepository.findOrderById(id);

        if (!Objects.equals(order.getUser().getId(), user.getId())) {
            return null;
        }

        return order;
    }

    // public PageImpl<Product> findAll(Pageable pageable) {
    //  List<Product> products = productRepository.findAll();
    //  return new PageImpl<>(products, pageable, products.size());
    // }

    public PageImpl<Order> getAllOrderAdmin(Pageable pageable) {
        List<Order> orders = orderRepository.findAll();
        return new PageImpl<>(orders, pageable, orders.size());
    }

    public Order getOrderById(Integer id) {
        return orderRepository.findOrderById(id);
    }

    public void updateStatusOrder(Integer id, Integer status) {
        Order order = orderRepository.findOrderById(id);
        OrderStatus orderStatus = orderStatusRepository.getOrderStatusById(status);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }

    public List<OrderStatus> getAllStatus() {
        return orderStatusRepository.findAll();
    }


    public List<OrderStatus> getAllStatus(Integer id) {
        return orderStatusRepository.findAll();
    }
}
