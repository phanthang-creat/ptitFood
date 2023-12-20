package com.server.ptitFood.domain.services;

import com.server.ptitFood.domain.dto.DiscountDto;
import com.server.ptitFood.domain.entities.Admin;
import com.server.ptitFood.domain.entities.Discount;
import com.server.ptitFood.domain.repositories.DiscountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;

    private final AdminControlService adminControlService;

    public DiscountService(
            DiscountRepository discountRepository,
            AdminControlService adminControlService
    ) {
        this.discountRepository = discountRepository;
        this.adminControlService = adminControlService;
    }

    public void createDiscount(DiscountDto discount) {
        Admin admin = adminControlService.getAdminByUserName();

        Discount newDiscount = new Discount();

        newDiscount.setCode(discount.getCode());
        newDiscount.setDiscount(discount.getDiscount());
        newDiscount.setLimitNumber(discount.getLimitNumber());
        newDiscount.setExpirationDate(discount.getExpirationDate());
        newDiscount.setPaymentLimit(discount.getPaymentLimit());
        newDiscount.setDescription(discount.getDescription());
        newDiscount.setStatus(discount.getStatus());
        newDiscount.setNumberUsed(0);
        newDiscount.setCreatedBy(admin);
        newDiscount.setCreated(new java.sql.Date(new java.util.Date().getTime()));
        newDiscount.setUpdatedBy(admin);
        newDiscount.setUpdated(new java.sql.Date(new java.util.Date().getTime()));

        discountRepository.save(newDiscount);
    }

    public Discount updateDiscount(DiscountDto discount) {
        Admin admin = adminControlService.getAdminByUserName();

        Discount newDiscount = discountRepository.findById(discount.getCode()).get();

        newDiscount.setDiscount(discount.getDiscount());
        newDiscount.setLimitNumber(discount.getLimitNumber());
        newDiscount.setExpirationDate(discount.getExpirationDate());
        newDiscount.setPaymentLimit(discount.getPaymentLimit());
        newDiscount.setDescription(discount.getDescription());
        newDiscount.setNumberUsed(0);
        newDiscount.setStatus(discount.getStatus());
        newDiscount.setUpdatedBy(admin);
        newDiscount.setUpdated(new java.sql.Date(new java.util.Date().getTime()));

        return discountRepository.save(newDiscount);
    }

    public Discount deleteDiscount(String code) {
        Discount discount = discountRepository.findById(code).get();

        discount.setStatus(0);

        return discountRepository.save(discount);
    }

    public Discount getDiscount(String code) {
        return discountRepository.findById(code).isPresent() ? discountRepository.findById(code).get() : null;
    }

    public List<Discount> getAllDiscount() {
        return discountRepository.findAll();
    }

    public List<Discount> getAllDiscountActive(){
        return discountRepository.getAvailableDiscounts();
    }

    public Discount getDiscountByCode(String code){
        return discountRepository.findByCode(code);
    }

    public Page<Discount> findAll(Pageable pageable) {
        return discountRepository.findAll(pageable);
    }
}
