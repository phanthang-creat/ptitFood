package com.server.ptitFood.domain.services;

import com.server.ptitFood.domain.dto.CategoryDto;
import com.server.ptitFood.domain.entities.Category;
import com.server.ptitFood.domain.repositories.CategoryRepository;
import com.server.ptitFood.domain.services.admin.AdminControlService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    final private CategoryRepository categoryRepository;

    final private AdminControlService adminService;

    public CategoryService(
            CategoryRepository categoryRepository,
            AdminControlService adminService
    ) {
        this.categoryRepository = categoryRepository;
        this.adminService = adminService;
    }

    public Page<Category> selectCategoriesDecryption(Pageable pageable) {
        List<Category> categories = categoryRepository.selectAllCategory();
        System.out.println(categories);
        return new PageImpl<>(categories, pageable, categories.size());
    }

    public List<Category> selectCategoriesDecryption() {
        return categoryRepository.selectAllCategory();
    }

    public void insertCategory(CategoryDto dto) {
        Integer id = adminService.getAdminByUserName().getId();

        categoryRepository.insertCategory(
                dto.getName(),
                dto.getLink(),
                dto.getParentId(),
                dto.getOrder(),
                id,
                id,
                dto.getStatus()
        );
    }

    public void updateCategory(CategoryDto dto) {
        Integer id = adminService.getAdminByUserName().getId();

        categoryRepository.updateCategory(
                dto.getId(),
                dto.getName(),
                dto.getLink(),
                dto.getParentId(),
                dto.getOrder(),
                id,
                dto.getStatus()
        );
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteCategory(
                id
        );
    }

    public List<Category> selectIdNameOrderCategoryDecryption() {
        return categoryRepository.selectIdNameOrderCategoryDecryption();
    }

    public Category selectCategoryDecryptionById(Integer id) {
        return categoryRepository.selectCategoryDecryptionById(id);
    }

}
