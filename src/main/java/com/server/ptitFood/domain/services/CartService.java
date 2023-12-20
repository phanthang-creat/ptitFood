package com.server.ptitFood.domain.services;

import com.server.ptitFood.domain.dto.CartDto;
import com.server.ptitFood.domain.entities.Cart;
import com.server.ptitFood.domain.entities.Customer;
import com.server.ptitFood.domain.entities.Product;
import com.server.ptitFood.domain.repositories.CartRepository;
import javassist.tools.web.BadHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final ProductService productService;

    private final UserService userService;

    public CartService(CartRepository cartRepository, ProductService productService, UserService userService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public List<Cart> add(CartDto cartDto) {
        Product product = productService.findProductById(cartDto.getProductId());

        if (product == null) {
            return null;
        }

        if (product.getNumberBuy() < cartDto.getQuantity()) {
            return null;
        }

        Customer customer = userService.getCurrentUser();

        Cart cart = cartRepository.findByUserAndProduct(customer, product);

        if (cart == null) {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(customer);
            cart.setQuantity(cartDto.getQuantity());
        } else {
            if (cart.getQuantity() + cartDto.getQuantity() > product.getNumberBuy()) {
                return null;
            }
            cart.setQuantity(cart.getQuantity() + cartDto.getQuantity());
        }

        cartRepository.save(cart);

        return cartRepository.findByUser(customer);
    }

    public boolean deleteCart(int id) {
        Customer customer = userService.getCurrentUser();

        Cart cart = cartRepository.findById(id).orElse(null);

        if (cart == null) {
            return false;
        }

        if (!Objects.equals(cart.getUser().getId(), customer.getId())) {
            return false;
        }

        cartRepository.delete(cart);

        return true;
    }

    public boolean deleteAllCart() {
        Customer customer = userService.getCurrentUser();

        cartRepository.deleteAllByUser(customer);

        return true;
    }

    public List<Cart> getCart() {
        Customer customer = userService.getCurrentUser();

        return cartRepository.findByUser(customer);
    }
}
