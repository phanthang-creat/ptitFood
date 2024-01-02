package com.server.ptitFood.controllers;

import com.server.ptitFood.domain.entities.Product;
import com.server.ptitFood.domain.services.CategoryService;
import com.server.ptitFood.domain.services.ProducerService;
import com.server.ptitFood.domain.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomePageCtl {

    final private ProductService productService;

    final private CategoryService categoryService;

    final private ProducerService producerService;

    public HomePageCtl(
            ProductService productService,
            CategoryService categoryService,
            ProducerService producerService
    ) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.producerService = producerService;
    }

    @ModelAttribute("categories")
    @Transactional
    public List<com.server.ptitFood.domain.entities.Category> getCategories() {
        return categoryService.selectCategoriesDecryption();
    }

    @RequestMapping("/")
    @Transactional
    public String index(
            Model model,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ) {

        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id"));

        Page<Product> resultPage = null;

        if (name == null) {
            resultPage = productService.findAll(pageable);
        } else {
            resultPage = productService.findAll(pageable, name);
        }

        int totalPages = resultPage.getTotalPages();

        if (totalPages > 0) {
            int start = 1;

            if (totalPages > 5) {
                start = totalPages - 5;
            }
            model.addAttribute("pageNumbers", Arrays.asList(start, totalPages));
        }

        model.addAttribute("nameSearch", name);

        model.addAttribute("currentPage", resultPage);

        List<Product> list = resultPage.getContent();

        model.addAttribute("items", list);
        model.addAttribute("number", resultPage.getNumber());

        return "web/portal/home/index";
    }

    @GetMapping(path = "gioi-thieu")
    public String about() {
        return "web/portal/home/about";
    }
}
