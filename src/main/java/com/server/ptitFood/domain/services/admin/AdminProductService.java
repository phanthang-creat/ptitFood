package com.server.ptitFood.domain.services.admin;

import com.server.ptitFood.domain.dto.ProductDto;
import com.server.ptitFood.domain.entities.Product;
import com.server.ptitFood.domain.repositories.ProductRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminProductService {
    final private ProductRepository productRepository;
    final private AdminControlService adminControlService;

    public AdminProductService(
            ProductRepository productRepository,
            AdminControlService adminControlService
    ) {
        this.productRepository = productRepository;
        this.adminControlService = adminControlService;
    }

    public void addProduct(ProductDto dto) {

        productRepository.insertProduct(
                dto.getCatId(),
                dto.getName(),
                dto.getAlias(),
                dto.getAvatar(),
                dto.getImage(),
                dto.getSortDesc(),
                dto.getDetail(),
                dto.getProducerId(),
                dto.getNumber(),
                dto.getNumberBuy(),
                dto.getPrice(),
                dto.getSale(),
                adminControlService.getAdminByUserName().getId(),
                adminControlService.getAdminByUserName().getId(),
                dto.getStatus()
        );
    }

    public PageImpl<Product> findAll(Pageable pageable) {
        List<Product> products = productRepository.selectProductDecryption();
        return new PageImpl<>(products, pageable, products.size());
    }

    public void findProductById() {
    }

    public void updateProduct() {
    }

    public void deleteProduct() {
    }

    public void findProductByCategory() {
    }

    public void findProductByCategoryAndName() {
    }


}
