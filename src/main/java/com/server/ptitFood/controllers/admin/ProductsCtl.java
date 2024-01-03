package com.server.ptitFood.controllers.admin;
import com.server.ptitFood.domain.dto.CategoryDto;
import com.server.ptitFood.domain.dto.ProducerDto;
import com.server.ptitFood.domain.dto.ProductDto;
import com.server.ptitFood.domain.entities.Product;
import com.server.ptitFood.domain.services.CategoryService;
import com.server.ptitFood.domain.services.ProducerService;
import com.server.ptitFood.domain.services.ProductService;
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

    final private ProductService adminProductService;

    final private ProducerService producerService;

    final private CategoryService categoryService;

    public ProductsCtl(
            ProductService adminProductService,
            ProducerService producerService, CategoryService categoryService) {
        this.adminProductService = adminProductService;
        this.producerService = producerService;
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
        }).collect(java.util.stream.Collectors.toList());
    }

    @ModelAttribute("producers")
    @Transactional
    public List<ProducerDto> getProducers() {
        return producerService.findAll().stream().map(item -> {
            ProducerDto dto = new ProducerDto();
            dto.setId(item.getId());
            dto.setName(item.getName());
            return dto;
        }).collect(java.util.stream.Collectors.toList());
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

    @GetMapping("edit/{id}")
    @Transactional
    public String edit(
            Model model,
            @PathVariable String id
//            @ModelAttribute("product") ProductDto dto
    ) {
        Product product = adminProductService.findProductById(Integer.valueOf(id));
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setPriceSale(product.getPriceSale());
        dto.setNumber(product.getNumber());
        dto.setNumberBuy(product.getNumberBuy());
        dto.setImg(product.getImg());
        dto.setAvatar(product.getAvatar());
        dto.setAlias(product.getAlias());
        dto.setDetail(product.getDetail());
        dto.setCatId(product.getCategory() != null ? product.getCategory().getId() : null);
        dto.setProducerId(product.getProducer() != null ? product.getProducer().getId() : null);
        dto.setStatus(product.getStatus());
        model.addAttribute("product", dto);
        return "web/admin/product/edit";
    }

    @PostMapping("edit/{id}")
    @Transactional
    public ModelAndView saveOrUpdate(
            ModelMap model,
            @Valid @ModelAttribute("product") ProductDto dto,
            @PathVariable Integer id,
            BindingResult result) {
        adminProductService.updateProduct(dto);
        System.out.println(dto);
        model.addAttribute("message", "Sản phẩm đã được lưu!");
        return new ModelAndView("redirect:/admin/products", model);
    }

    @GetMapping("delete/{id}")
    @Transactional
    public ModelAndView delete(
            ModelMap model,
            @PathVariable Integer id) {
        adminProductService.deleteProduct(id);
        model.addAttribute("message", "Sản phẩm đã được xóa!");
        return new ModelAndView("redirect:/admin/products", model);
    }
}
