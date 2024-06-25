package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// REST controller for shopping cart functionalities
@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    // GET method to retrieve the user's shopping cart
    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        try {
            // get the currently logged-in username
            String userName = principal.getName();
            // find the database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingCartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added)
    @PostMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addProductToCart(@PathVariable int productId, Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // Check if the product is already in the cart
            ShoppingCartItem existingItem = shoppingCartDao.getCartItem(userId, productId);
            if (existingItem == null) {
                // If not, add it to the cart with a quantity of 1
                shoppingCartDao.addProductToCart(userId, productId, 1);
            } else {
                // If it exists, increase the quantity by 1
                shoppingCartDao.updateProductInCart(userId, productId, existingItem.getQuantity() + 1);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateProductInCart(@PathVariable int productId, @RequestBody ShoppingCartItem cartItem, Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            ShoppingCartItem existingItem = shoppingCartDao.getCartItem(userId, productId);
            if (existingItem != null) {
                // Only update if the product is already in the cart
                shoppingCartDao.updateProductInCart(userId, productId, cartItem.getQuantity());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found in cart");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // DELETE method to clear all products from the current user's cart
    // https://localhost:8080/cart
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.clearCart(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops...");
        }
    }
}
