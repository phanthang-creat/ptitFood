package com.server.ptitFood.controllers;

import com.server.ptitFood.domain.entities.Product;
import com.server.ptitFood.domain.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductCtl {

    final private ProductService productService;

    public ProductCtl(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("{alias}")
    public String getProduct(
            @PathVariable("alias") String alias,
            Model model
    ) {
        if (alias == null) {
            return "redirect:/";
        }

        if (alias.equals("all")) {
            return "redirect:/";
        }

        Product product = productService.findByAlias(alias);

        if (product == null) {
            return "redirect:/error?code=404";
        }

        model.addAttribute("product", product);

        return "web/portal/product/index";
    }
}
