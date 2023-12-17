package com.server.ptitFood.controllers.admin;
import com.server.ptitFood.domain.dto.CategoryDto;
import com.server.ptitFood.domain.dto.ProducerDto;
import com.server.ptitFood.domain.dto.ProductDto;
import com.server.ptitFood.domain.entities.Product;
import com.server.ptitFood.domain.services.CategoryService;
import com.server.ptitFood.domain.services.admin.AdminProducerService;
import com.server.ptitFood.domain.services.admin.AdminProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "admin/products")
public class ProductsCtl {

    final private AdminProductService adminProductService;

    final private AdminProducerService adminProducerService;

    final private CategoryService categoryService;

    public ProductsCtl(
            AdminProductService adminProductService,
            AdminProducerService adminProducerService, CategoryService categoryService) {
        this.adminProductService = adminProductService;
        this.adminProducerService = adminProducerService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("categories")
    @Transactional
    public List<CategoryDto> getCategories() {
        return categoryService.selectCategoriesDecryption().stream().map(item -> {
            CategoryDto dto = new CategoryDto();
            dto.setId(item.getId());
            dto.setName(item.getName());
            return dto;
        }).toList();
    }

    @ModelAttribute("producers")
    @Transactional
    public List<ProducerDto> getProducers() {
        return adminProducerService.findAll().stream().map(item -> {
            ProducerDto dto = new ProducerDto();
            dto.setId(item.getId());
            dto.setName(item.getName());
            return dto;
        }).toList();
    }

    @Transactional
    @RequestMapping("")
    public String productsPage(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model
    ) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id"));

        Page<Product> resultPage = null;

        if (name != null) {
            resultPage = adminProductService.findAll(pageable);
            model.addAttribute("name", name);
        } else {
            resultPage = adminProductService.findAll(pageable);
        }

        System.out.println("resultPage" + resultPage);

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

        model.addAttribute("products", resultPage.getContent());
        model.addAttribute("productPage", resultPage);

        System.out.println(resultPage.getContent());

        return "web/admin/product/search";
    }

    @GetMapping("add")
    @Transactional
    public String add(Model model) {
        model.addAttribute("currentDto", new ProductDto());

        return "web/admin/product/add";
    }

    @PostMapping("add")
    @Transactional
    public ModelAndView add(
            ModelMap model,
            @Valid @ModelAttribute("currentDto") ProductDto dto,
            BindingResult result) {
        adminProductService.addProduct(dto);
        model.addAttribute("message", "Sản phẩm đã được lưu!");
        return new ModelAndView("redirect:/admin/products", model);
    }

    @PostMapping("edit")
    @Transactional
    public ModelAndView saveOrUpdate(
            ModelMap model,
            @Valid @ModelAttribute("customer") ProductDto dto,
            BindingResult result) {

        System.out.println(dto);
        System.out.println(result.hasErrors());

//        Optional<Product> khsv = adminProductService;
//        if(khsv.isPresent() && !dto.getEdit()){
//            model.addAttribute("message", "CMND đã tồn tại!");
//            return new ModelAndView("/customers/addOrEdit");
//
//        }
//        if (result.hasErrors()) {
//            return new ModelAndView("/customers/addOrEdit");
//        }
//
//        KhachHang entity = new KhachHang();
//        BeanUtils.copyProperties(dto, entity);
//        System.out.println(entity);
//        customerService.save(entity);
//        model.addAttribute("message", "customers is saved!");
//        return new ModelAndView("redirect:/manager/customers", model);
        return null;
    }
}
