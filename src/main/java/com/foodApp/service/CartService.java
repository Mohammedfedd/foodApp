package com.foodApp.service;

import com.foodApp.model.Cart;
import com.foodApp.model.CartItem;
import com.foodApp.request.AddCartItemRequest;

public interface CartService {
    public CartItem addItemToCart(AddCartItemRequest req, String jwt)throws Exception;
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity)throws Exception;
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception;
    public Long calculateCartTotals(Cart cart) throws Exception;
    public Cart findCartById(Long id) throws Exception;
    public Cart findCartByUserId(Long userId) throws Exception;
    public Cart clearCart(Long userId) throws Exception;
    public void deleteCart(Long id);
    public void deleteCartEntriesByRestaurantId(Long restaurantId);
}
