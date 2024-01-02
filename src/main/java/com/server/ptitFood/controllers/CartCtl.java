package com.server.ptitFood.controllers;

import com.server.ptitFood.domain.dto.CartDto;
import com.server.ptitFood.domain.dto.OrderDto;
import com.server.ptitFood.domain.entities.Cart;
import com.server.ptitFood.domain.entities.Discount;
import com.server.ptitFood.domain.entities.Order;
import com.server.ptitFood.domain.services.CartService;
import com.server.ptitFood.domain.services.DiscountService;
import com.server.ptitFood.domain.services.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(path = "cart")
public class CartCtl {
    private final CartService cartService;

    private final DiscountService discountService;

    private final OrderService orderService;

    public CartCtl(CartService cartService, DiscountService discountService, OrderService orderService) {
        this.cartService = cartService;
        this.discountService = discountService;
        this.orderService = orderService;
    }

    @PostMapping(path = "add")
    public String add(
            @Valid CartDto cartDto,
            HttpSession session,
            Model model
    ) {

        HashMap<Integer, Integer> cartItems = session.getAttribute("cart") == null ?
                new HashMap<>() : (HashMap<Integer, Integer>) session.getAttribute("cart");
        if (cartService.add(cartDto) != null) {
            if (cartItems.containsKey(cartDto.getProductId())) {
                cartItems.put(cartDto.getProductId(), cartItems.get(cartDto.getProductId()) + cartDto.getQuantity());
            } else {
                cartItems.put(cartDto.getProductId(), cartDto.getQuantity());
            }
        } else {
            model.addAttribute("error", "Số lượng sản phẩm không đủ");
            return "redirect:/product/" + cartDto.getAlias();
        }

        return "redirect:/cart/detail";
    }


    @PostMapping(path = "delete/{id}")
    @ResponseBody
    public HashMap<String, String> delete(
            @PathVariable("id") int id
    ) {
        HashMap<String, String> map = new HashMap<>();

        if (cartService.deleteCart(id)) {
            map.put("status", "success");
        } else {
            map.put("status", "fail");
        }

        return map;
    }

    @PostMapping(path = "deleteAll")
    @ResponseBody
    public HashMap<String, String> deleteAll() {
        HashMap<String, String> map = new HashMap<>();

        if (cartService.deleteAllCart()) {
            map.put("status", "success");
        } else {
            map.put("status", "fail");
        }

        return map;
    }

    @GetMapping(path = "detail")
    public String cart(
            Model model
    ) {

        List<Cart> carts = cartService.getCart();
        System.out.println(carts);
        model.addAttribute("carts", carts);

        int total = 0;
        for (Cart cart : carts) {
            total += cart.getProduct().getPriceSale() * cart.getQuantity();
        }
        model.addAttribute("total", total);

        return "web/portal/cart/detail";
    }

//    check out
    @GetMapping(path = "checkout")
    public String checkout(
            Model model,
            @RequestParam(name = "code", required = false) String code,
            HttpSession session
    ) {

        List<Cart> carts = cartService.getCart();
        System.out.println(carts);
        model.addAttribute("carts", carts);

        List<Discount> discounts = discountService.getAllDiscountActive();
        model.addAttribute("discounts", discounts);


        int total = 0;
        for (Cart cart : carts) {
            total += cart.getProduct().getPriceSale() * cart.getQuantity();

        }

        if (code != null) {
            Discount discount = discountService.getDiscountByCode(code);
            if (discount != null) {
                model.addAttribute("discount", discount);
                total = total - discount.getDiscount();
            }
        }

        model.addAttribute("total", total);

        model.addAttribute("orderDto", new OrderDto());

        return "web/portal/cart/checkout";
    }

    @PostMapping(path = "checkout")
    public String checkout(
            Model model,
            HttpSession session,
            OrderDto orderDto
            ) {
        HashMap<Integer, Integer> cartItems = session.getAttribute("cart") == null ?
                new HashMap<>() : (HashMap<Integer, Integer>) session.getAttribute("cart");
        cartItems.clear();
        session.setAttribute("cart", cartItems);
        if (orderService.createOrder(orderDto)) {
            return "redirect:/cart/success";
        } else {
            return "redirect:/cart/checkout";
        }
    }

    @GetMapping(path = "success")
    public String success(
            Model model
    ) {
        return "web/portal/cart/success";
    }
}
