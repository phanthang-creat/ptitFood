package com.server.ptitFood.controllers.admin;

import com.server.ptitFood.domain.dto.CustomerDto;
import com.server.ptitFood.domain.entities.Customer;
import com.server.ptitFood.domain.services.UserService;
import com.server.ptitFood.domain.services.AdminControlService;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "admin/customers")
@Transactional
public class CustomersCtl {

    final private UserService userService;

    public CustomersCtl(AdminControlService adminControlService, UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(name = "")
    @Transactional(readOnly = true)
    public String customers(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model
    ) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id"));

        Page<Customer> resultPage = null;

        if (username != null) {
            resultPage = userService.findByUsernameContaining(username, pageable);
            model.addAttribute("username", username);
        } else {
            resultPage = userService.selectCustomerDecryption(pageable);
        }

        int totalPages = resultPage.getTotalPages();

        if (totalPages > 0) {
            int start = 1;

            if (totalPages > 5) {
                start = totalPages - 5;
            }
            model.addAttribute("pageNumbers", Arrays.asList(start, totalPages));
        }

        model.addAttribute("accountPage", resultPage);

        List<Customer> list = resultPage.getContent();
        List<CustomerDto> listDto = new ArrayList<>();
        for (Customer customer : list) {
            CustomerDto dto = getCustomerDto(customer);
            listDto.add(dto);
        }

        model.addAttribute("customerDto", listDto);
        model.addAttribute("customers", list);
        model.addAttribute("number", resultPage.getNumber());

        return "web/admin/customers/search";
    }

    @NotNull
    private CustomerDto getCustomerDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
//            dto.setId(customer.getId());
        dto.setName(customer.getFullName());
        dto.setUsername(customer.getUsername());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setRole(customer.getUserGroup().getName());
        return dto;
    }
}
