package com.server.ptitFood.controllers;

import com.server.ptitFood.domain.dto.ProfileDto;
import com.server.ptitFood.domain.dto.UpdatePasswordDto;
import com.server.ptitFood.domain.entities.Order;
import com.server.ptitFood.domain.entities.OrderDetail;
import com.server.ptitFood.domain.exceptions.UsernameOrPasswordNotValid;
import com.server.ptitFood.domain.services.OrderDetailService;
import com.server.ptitFood.domain.services.OrderService;
import com.server.ptitFood.domain.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("orders", orderService.getAllOrder());
        return "web/portal/profile/index";
    }

    @RequestMapping(path = "/order/{id}")
    public String orderDetail(
            Model model,
            @PathVariable Integer id
    ) {
        model.addAttribute("user", userService.getCurrentUser());
        Order order =  orderService.getDecryptionById(id);
        model.addAttribute("order", order);
        List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetail(order);
        model.addAttribute("orderDetails", orderDetails);

        return "web/portal/profile/order-detail";
    }

    @PostMapping(path = "/update")
    public String update(
            Model model,
            ProfileDto profileDto
    ) throws UsernameOrPasswordNotValid {
        try {
            userService.update(profileDto);
            model.addAttribute("user", userService.getCurrentUser());
            return "web/portal/profile/index";
        } catch (UsernameOrPasswordNotValid e) {
            model.addAttribute("user", userService.getCurrentUser());
            model.addAttribute("error", e.getMessage());
            return "web/portal/profile/index";
        }
    }

    @PostMapping(path = "/update-password")
    public String updatePassword(
            Model model,
            UpdatePasswordDto updatePasswordDto
    ) throws UsernameOrPasswordNotValid {
        try {
            userService.updatePassword(updatePasswordDto);
            model.addAttribute("user", userService.getCurrentUser());
            return "web/portal/profile/index";
        } catch (UsernameOrPasswordNotValid e) {
            model.addAttribute("user", userService.getCurrentUser());
            model.addAttribute("passwordError", e.getMessage());
            return "web/portal/profile/index";
        }
    }
}
