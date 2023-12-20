package com.server.ptitFood.controllers.admin;

import com.server.ptitFood.domain.dto.ProducerDto;
import com.server.ptitFood.domain.entities.Producer;
import com.server.ptitFood.domain.services.ProducerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping(path = "admin/producers")
@Transactional
public class ProducersCtl {

    final private ProducerService producerService;

    public ProducersCtl(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping(name = "", path = "")
    @Transactional()
    public String producersPage(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model
    ) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id"));

        Page<Producer> resultPage = null;

//        if (name != null) {
//            resultPage = adminProductService.findAll(pageable);
//            model.addAttribute("name", name);
//        } else {
//            resultPage = adminProductService.findAll(pageable);
//        }

        resultPage = producerService.findAll(pageable);

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

        System.out.println(resultPage.getContent());

        return "web/admin/producer/index";
    }

    @RequestMapping("/add")
    public String addProducersPage(Model model) {
        model.addAttribute("producer", new ProducerDto());
        return "web/admin/producer/add";
    }

    @PostMapping("/add")
    @Transactional()
    public String addProducersPage(@Valid ProducerDto producerDto, BindingResult result, Model model) {
        System.out.println("producerDto: " + producerDto.toString());
        producerService.insertProducer(producerDto);
        if (result.hasErrors()) {
            return "web/admin/producer/add";
        }
        return "redirect:/admin/producers/add";
    }

    @GetMapping("/edit/{id}")
    @Transactional()
    public String editProducersPage(
            Model model,
            @PathVariable String id
    ) {
        Producer producer = producerService.findById(Integer.valueOf(id));
        ProducerDto dto = new ProducerDto();
        dto.setId(producer.getId());
        dto.setName(producer.getName());
        dto.setCode(producer.getCode());
        dto.setKeyword(producer.getKeyword());
        dto.setStatus(producer.getStatus());
        dto.setCreated(producer.getCreated());
        dto.setUpdated(producer.getUpdated());
        dto.setCreatedBy(producer.getCreatedBy());
        dto.setUpdatedBy(producer.getUpdatedBy());

        model.addAttribute("producer", dto);
        return "web/admin/producer/edit";
    }

    @PostMapping("/edit/{id}")
    @Transactional()
    public String editProducersPage(
            @Valid ProducerDto producerDto,
            BindingResult result,
            Model model,
            @PathVariable String id
    ) {
        System.out.println("producerDto: " + producerDto.toString());
        producerService.updateProducerById(producerDto);
        if (result.hasErrors()) {
            return "web/admin/producer/edit";
        }
        return "redirect:/admin/producers/edit/" + id;
    }

    @PostMapping("/delete/{id}")
    @Transactional()
    public String deleteProducersPage(
            @PathVariable String id
    ) {
        producerService.deleteProducerById(Integer.valueOf(id));
        return "redirect:/admin/producers";
    }
}
