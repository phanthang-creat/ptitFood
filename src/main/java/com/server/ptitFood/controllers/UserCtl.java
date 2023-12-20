package com.server.ptitFood.controllers;

import com.server.ptitFood.domain.entities.Order;
import com.server.ptitFood.domain.entities.OrderDetail;
import com.server.ptitFood.domain.services.OrderDetailService;
import com.server.ptitFood.domain.services.OrderService;
import com.server.ptitFood.domain.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "profile")
public class UserCtl {

    private final UserService userService;

    private final OrderService orderService;

    private final OrderDetailService orderDetailService;

    public UserCtl(
            UserService userService,
            OrderService orderService,
            OrderDetailService orderDetailService
    ) {
        this.userService = userService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    @RequestMapping(path = "")
    public String index(
            Model model
    ) {
        model.addAttribute("user", userService.getCustomerDecryptionByUsername());
        model.addAttribute("orders", orderService.getAllOrder());
        return "web/portal/profile/index";
    }

    @RequestMapping(path = "/order/{id}")
    public String orderDetail(
            Model model,
            @PathVariable Integer id
    ) {
        model.addAttribute("user", userService.getCustomerDecryptionByUsername());
        System.out.println(userService.getCustomerDecryptionByUsername());
        Order order =  orderService.getDecryptionById(id);
        model.addAttribute("order", order);
        List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetail(order);
        model.addAttribute("orderDetails", orderDetails);

        return "web/portal/profile/order-detail";
    }
}
