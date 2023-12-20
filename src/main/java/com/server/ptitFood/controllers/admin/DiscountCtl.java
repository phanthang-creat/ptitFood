package com.server.ptitFood.controllers.admin;

import com.server.ptitFood.domain.dto.DiscountDto;
import com.server.ptitFood.domain.entities.Discount;
import com.server.ptitFood.domain.services.DiscountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping("/admin/discounts")
public class DiscountCtl {

    private final DiscountService discountService;

    public DiscountCtl(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping("")
    public String discountPage(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam(name = "code", required = false) String code
    ) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id"));

        Page<Discount> resultPage = null;

//        if (name != null) {
//            resultPage = adminProductService.findAll(pageable);
//            model.addAttribute("name", name);
//        } else {
//            resultPage = adminProductService.findAll(pageable);
//        }

        resultPage = discountService.findAll(pageable);

        System.out.println("resultPage" + resultPage);

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = 0;

            if (totalPages > 5) {
                start = totalPages - 5;
            }

            model.addAttribute("start", start);
            model.addAttribute("end", totalPages);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", resultPage);
            model.addAttribute("pageNumbers", Arrays.asList(start, totalPages));
            model.addAttribute("items", resultPage.getContent());
        }

        return "web/admin/discount/index";
    }

    @GetMapping("add")
    public String add(Model model) {
        model.addAttribute("currentDto", new DiscountDto());

        return "web/admin/discount/add";
    }

    @PostMapping("add")
    public String add(DiscountDto discountDto) {
        discountService.createDiscount(discountDto);

        return "redirect:/admin/discounts";
    }
}
