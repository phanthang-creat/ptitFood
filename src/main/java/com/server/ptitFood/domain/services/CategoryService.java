package com.server.ptitFood.domain.services;

import com.server.ptitFood.domain.dto.CategoryDto;
import com.server.ptitFood.domain.entities.Admin;
import com.server.ptitFood.domain.entities.Category;
import com.server.ptitFood.domain.repositories.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
        List<Category> categories = categoryRepository.findAll();
        return new PageImpl<>(categories, pageable, categories.size());
    }

    public List<Category> selectCategoriesDecryption() {
        return categoryRepository.findAll();
    }

    public void insertCategory(CategoryDto dto) {
        Admin id = adminService.getAdminByUserName();

        Category category = new Category();
        category.setName(dto.getName());
        category.setLink(dto.getLink());
        category.setParentId(
                dto.getParentId() != null &&
                categoryRepository.findById(dto.getParentId()).isPresent() ?
                        categoryRepository.findById(dto.getParentId()).get() : null
        );
        category.setSortDesc(dto.getSortDesc());
        category.setCreatedBy(id);
        category.setUpdatedBy(id);
        category.setStatus(dto.getStatus());
        category.setUpdated(new Date(System.currentTimeMillis()));
        category.setCreated(new Date(System.currentTimeMillis()));
        categoryRepository.save(category);
    }

    public void updateCategory(CategoryDto dto) {
        Admin id = adminService.getAdminByUserName();

        Category category = categoryRepository.findById(dto.getId()).isPresent() ?
                categoryRepository.findById(dto.getId()).get() : null;
        assert category != null;
        category.setName(dto.getName());
        category.setLink(dto.getLink());
        category.setParentId(
                dto.getParentId() != null &&
                        categoryRepository.findById(dto.getParentId()).isPresent() ?
                        categoryRepository.findById(dto.getParentId()).get() : null
        );
        category.setSortDesc(dto.getSortDesc());
        category.setUpdatedBy(id);
        category.setStatus(dto.getStatus());
        categoryRepository.save(category);
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
        return categoryRepository.findById(id).isPresent() ?
                categoryRepository.findById(id).get() : null;
    }

    public Category selectCategoryDecryptionByIdQuery(Integer id) {
        return categoryRepository.selectCategoryDecryptionByIdQuery(id);
    }

}
