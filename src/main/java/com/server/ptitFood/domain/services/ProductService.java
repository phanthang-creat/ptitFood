package com.server.ptitFood.domain.services;

import com.server.ptitFood.domain.dto.ProductDto;
import com.server.ptitFood.domain.entities.Admin;
import com.server.ptitFood.domain.entities.Product;
import com.server.ptitFood.domain.repositories.ProductRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ProductService {
    final private ProductRepository productRepository;
    final private AdminControlService adminControlService;

    final private CategoryService cate;

    final private ProducerService producerService;

    public ProductService(
            ProductRepository productRepository,
            AdminControlService adminControlService,
            CategoryService cate, ProducerService producerService) {
        this.productRepository = productRepository;
        this.adminControlService = adminControlService;
        this.cate = cate;
        this.producerService = producerService;
    }

    public void addProduct(ProductDto dto) {
        Admin id = adminControlService.getAdminByUserName();

        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(cate.selectCategoryDecryptionById(dto.getCatId()));
        product.setProducer(producerService.selectProductDecryptionById(dto.getProducerId()));
        product.setAlias(dto.getAlias());
        product.setDetail(dto.getDetail());
        product.setPrice(dto.getPrice());
        product.setPriceSale(dto.getPriceSale());
        product.setNumber(dto.getNumber());
        product.setNumberBuy(dto.getNumberBuy());
        product.setImg(dto.getImg());
        product.setAvatar(dto.getAvatar());
        product.setCreatedBy(id);
        product.setUpdatedBy(id);
        product.setStatus(dto.getStatus());
        product.setCreated(new Date(System.currentTimeMillis()));
        product.setUpdated(new Date(System.currentTimeMillis()));
        productRepository.save(product);
    }

    public PageImpl<Product> findAll(Pageable pageable) {
        List<Product> products = productRepository.findAll();
        return new PageImpl<>(products, pageable, products.size());
    }

    public PageImpl<Product> findAll(Pageable pageable, String name) {
        List<Product> products = productRepository.findAllByNameContainsAndStatus(name, 1);

        return new PageImpl<>(products, pageable, products.size());
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findProductById(
            Integer id) {
        return productRepository.findById(id).isPresent() ? productRepository.findById(id).get() : null;
    }

    public Product findByAlias(String alias) {
        return productRepository.findProductByAliasAndStatus(alias, 1);
    }

    public void save(Product product) {
        productRepository.save(product);
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
