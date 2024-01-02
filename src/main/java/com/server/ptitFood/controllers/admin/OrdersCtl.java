package com.server.ptitFood.controllers.admin;

import com.server.ptitFood.domain.entities.Order;
import com.server.ptitFood.domain.entities.OrderDetail;
import com.server.ptitFood.domain.entities.OrderStatus;
import com.server.ptitFood.domain.services.CartService;
import com.server.ptitFood.domain.services.DiscountService;
import com.server.ptitFood.domain.services.OrderDetailService;
import com.server.ptitFood.domain.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "admin/orders")
public class OrdersCtl {
    private final CartService cartService;

    private final DiscountService discountService;

    private final OrderService orderService;

    private final OrderDetailService orderDetailService;

    public OrdersCtl(CartService cartService, DiscountService discountService, OrderService orderService, OrderDetailService orderDetailService) {
        this.cartService = cartService;
        this.discountService = discountService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    @ModelAttribute("orderStatus")
    public List<OrderStatus> orderStatus() {
        return orderService.getAllStatus();
    }

    @GetMapping(path = "")
    public String index(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("name") Optional<String> name,
            ModelMap model
    ) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id"));

        Page<Order> resultPage = null;


        resultPage = orderService.getAllOrderAdmin(pageable);


        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = 1;

            if (totalPages > 5) {
                start = totalPages - 5;
            }

            model.addAttribute("start", start);
            model.addAttribute("end", totalPages);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", resultPage);
            model.addAttribute("pageNumbers", Arrays.asList(start, totalPages));

        }

        model.addAttribute("orders", resultPage.getContent());
        model.addAttribute("orderPage", resultPage);
        return "web/admin/order/index";
    }


    @GetMapping(path = "/edit/{id}")
    public String detail(

            @PathVariable("id") Integer id,
            ModelMap model
    ) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetail(order);
        model.addAttribute("orderDetails", orderDetails);
        return "web/admin/order/detail";
    }

    @PostMapping(path = "/edit/{id}")
    public String update(
            @PathVariable("id") Integer id,
            Integer status,
            ModelMap model
    ) {
        orderService.updateStatusOrder(id, status);

        return "redirect:/admin/orders";
    }

}
