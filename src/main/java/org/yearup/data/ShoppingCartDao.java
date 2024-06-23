package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);

    void clearCart(int userId);

    void updateProductInCart(int userId, int productId, int quantity);
    // add additional method signatures here
}
