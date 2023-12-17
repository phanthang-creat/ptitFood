package com.server.ptitFood.controllers.admin;

import com.server.ptitFood.domain.dto.CategoryDto;
import com.server.ptitFood.domain.dto.CustomerDto;
import com.server.ptitFood.domain.entities.Category;
import com.server.ptitFood.domain.entities.Customer;
import com.server.ptitFood.domain.services.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/categories")
@Transactional
public class CategoriesCtl {

    final private CategoryService categoryService;

    public CategoriesCtl(
            CategoryService categoryService
    ) {
        this.categoryService = categoryService;
    }

    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryService.selectCategoriesDecryption();
    }

    @GetMapping("")
    @Transactional()
    public String categories(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model
    ) {

        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id"));

        Page<Category> resultPage = categoryService.selectCategoriesDecryption(pageable);

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

        return "web/admin/category/index";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("category", new CategoryDto());
        return "web/admin/category/add";
    }

    @PostMapping("/add")
    public String add(CategoryDto dto, Model model) {
        categoryService.insertCategory(dto);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Category category = categoryService.selectCategoryDecryptionById(id);
        model.addAttribute("currentDto", getCustomerDto(category));
        return "web/admin/category/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            CategoryDto dto, Model model,
            @PathVariable("id") Integer id
    ) {
        System.out.println(dto);
        categoryService.updateCategory(dto);
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Model model) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    private CategoryDto getCustomerDto(Category customer) {
        CategoryDto dto = new CategoryDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setLink(customer.getLink());
        dto.setParentId(customer.getParentId() == null ? null : customer.getParentId().getId());
        dto.setStatus(customer.getStatus());
        return dto;
    }
}
