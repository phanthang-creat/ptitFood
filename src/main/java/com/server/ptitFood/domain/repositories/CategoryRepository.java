package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Procedure(procedureName = "insert_category")
    void insertCategory(
            @Param("name_in") String name,
            @Param("link_in") String link,
            @Param("parent_id_in") Integer parentId,
            @Param("order_in") Integer order,
            @Param("created_by_in") Integer createdBy,
            @Param("updated_by_in") Integer updatedBy,
            @Param("status_in") Integer status
    );

    @Procedure(procedureName = "update_category_by_id")
    void updateCategory(
            @Param("id_in") Integer id,
            @Param("name_in") String name,
            @Param("link_in") String link,
            @Param("parent_id_in") Integer parentId,
            @Param("order_in") Integer order,
            @Param("updated_by_in") Integer updatedBy,
            @Param("status_in") Integer status
    );

    @Procedure(procedureName = "delete_category")
    void deleteCategory(
            @Param("id_in") Integer id
    );

    @Procedure(procedureName = "select_category_decryption")
    List<Category> selectAllCategory();

    @Procedure(procedureName = "select_id_name_order_category_decryption")
    List<Category> selectIdNameOrderCategoryDecryption();

    @Procedure(procedureName = "select_category_decryption_by_id")
    Category selectCategoryDecryptionById(
            @Param("id_in") Integer id
    );

}
