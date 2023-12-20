package com.server.ptitFood.domain.services;

import com.server.ptitFood.domain.dto.OrderDto;
import com.server.ptitFood.domain.entities.*;
import com.server.ptitFood.domain.repositories.OrderDetailRepository;
import com.server.ptitFood.domain.repositories.OrderRepository;
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

    private final AdminControlService adminControlService;

    private final CartService cartService;

    public OrderService(
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository, ProductService productService,
            DiscountService discountService,
            UserService userService,
            AdminControlService adminControlService,
            CartService cartService
    ) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
        this.discountService = discountService;
        this.userService = userService;
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
            product.setNumberBuy(product.getNumberBuy() - carts.get(currentProductsInRepository.indexOf(product)).getQuantity());
            productService.save(product);
        }

        int id = orderRepository.insertOrder(
                user.getId(),
                orderDto.getFullname(),
                orderDto.getPhone(),
                orderDto.getMoney(),
                discount == null ? null : discount.getId(),
                orderDto.getAddress(),
                1
        );

        for (Product product : currentProductsInRepository) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(orderRepository.findById(id));
            orderDetail.setProduct(product);
            orderDetail.setCount(carts.get(currentProductsInRepository.indexOf(product)).getQuantity());
            orderDetail.setPrice(product.getPrice());
            orderDetail.setStatus(1);
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

        Order order = orderRepository.selectOrderDecryptionById(id);

        if (!Objects.equals(order.getUser().getId(), user.getId())) {
            return null;
        }

        return order;
    }


}
