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
import java.util.Optional;

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

        if (dto.getNumberBuy() > dto.getNumber()) {
            throw new RuntimeException("Số lượng sản phẩm không đủ");
        }

        Product currentProduct = productRepository.findProductByAliasAndStatus(dto.getAlias(), 1);

        if (currentProduct != null) {
            throw new RuntimeException("Alias đã tồn tại");
        }

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
    public void updateProduct(ProductDto dto) {
        Admin id = adminControlService.getAdminByUserName();

        Optional<Product> product = productRepository.findById(dto.getId());

        if (dto.getNumberBuy() > dto.getNumber()) {
            throw new RuntimeException("Số lượng sản phẩm không đủ");
        }

        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        } else {
            product.get().setName(dto.getName());
            product.get().setPrice(dto.getPrice());
            product.get().setCategory(cate.selectCategoryDecryptionById(dto.getCatId()));
            product.get().setProducer(producerService.selectProductDecryptionById(dto.getProducerId()));
            product.get().setDetail(dto.getDetail());
            product.get().setPrice(dto.getPrice());
            product.get().setPriceSale(dto.getPriceSale());
            product.get().setNumber(dto.getNumber());
            product.get().setNumberBuy(dto.getNumberBuy());
            product.get().setImg(dto.getImg());
            product.get().setAvatar(dto.getAvatar());
            product.get().setUpdatedBy(id);
            product.get().setStatus(dto.getStatus());
            product.get().setUpdated(new Date(System.currentTimeMillis()));
            productRepository.save(product.get());
        }
    }

    public void deleteProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        } else {
            product.get().setStatus(0);
            productRepository.save(product.get());
        }
    }

    public void findProductByCategory() {
    }

    public void findProductByCategoryAndName() {
    }


}
