package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, String> {

    @Query("SELECT d FROM Discount d WHERE d.status = 1 AND d.expirationDate >= CURRENT_DATE AND d.limitNumber > d.numberUsed")
    List<Discount> getAvailableDiscounts();

    @Query("SELECT d FROM Discount d WHERE d.status = 1 AND d.expirationDate >= CURRENT_DATE AND d.limitNumber > d.numberUsed AND d.code = ?1")
    Discount findByCode(String code);
}
