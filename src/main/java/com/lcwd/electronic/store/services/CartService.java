package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {


    //add items to card:
    //case1:cart for user is not available:we will create the cart and then add the item
    //cart 2:cart available add the items to cart

    CartDto addItemToCart( String userId,AddItemToCartRequest request);

    //remove item from cart:
    void removeItemFromCart(String userId,int cartItem);
    void clearCart(String userId);
}