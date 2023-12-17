package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Procedure(procedureName = "select_product_decryption")
    List<Product> selectProductDecryption();

    @Procedure(procedureName = "insert_product")
    void insertProduct(
            @Param("catid") Integer category,
            @Param("name") String name,
            @Param("alias") String avatar,
            @Param("avatar") String description,
            @Param("img") String image,
            @Param("sort_desc") Integer sortDesc,
            @Param("detail") String detail,
            @Param("producer") Integer producer,
            @Param("number") Integer number,
            @Param("number_buy") Integer numberBuy,
            @Param("price") Integer price,
            @Param("price_sale") Integer sale,
            @Param("create_by") Integer createBy,
            @Param("update_by") Integer updateBy,
            @Param("status") Integer status
    );

}
