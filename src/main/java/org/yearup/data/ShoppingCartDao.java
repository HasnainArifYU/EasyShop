package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here

    void clearCart(int userId);

    void updateProductInCart(int userId, int productId, int quantity);

    void addProductToCart(int userId, int productId, int i);

    ShoppingCartItem getCartItem(int userId, int productId);

}
