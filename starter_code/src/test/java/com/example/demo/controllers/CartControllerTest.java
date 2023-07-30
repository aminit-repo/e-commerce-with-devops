package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.People;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.apache.coyote.Response;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartControllerTest {

    private UserRepository userRepository = mock(UserRepository.class);


    private CartRepository cartRepository= mock(CartRepository.class);

    private ItemRepository itemRepository= mock(ItemRepository.class);
    private CartController cartController = new CartController();

    @BeforeEach
    void setUp() {
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    void addTocart() {
        ModifyCartRequest modifyCartRequest= new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("JWT");
        modifyCartRequest.setQuantity(2);

        Cart newCart= new Cart();
        newCart.setItems(new ArrayList<>());
        People user= new People(modifyCartRequest.getUsername(), newCart, "password");
        newCart.setUser(user);


        Item item= new Item();
        item.setId(1L);
        item.setName("Shoe");
        item.setPrice(new BigDecimal(200));

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity response= cartController.addTocart(modifyCartRequest);
        Assert.assertEquals(200, response.getStatusCode().value());
        Cart cart= (Cart)response.getBody();
        Assert.assertEquals("JWT",cart.getUser().getUsername());
        Assert.assertEquals(BigDecimal.valueOf(400L),cart.getTotal());

    }

    @Test
    void removeFromcart() {
        ModifyCartRequest modifyCartRequest= new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("JWT");
        modifyCartRequest.setQuantity(1);

        Cart newCart= new Cart();
        newCart.setItems(new ArrayList<>());
        newCart.setId(1L);
        People user= new People(modifyCartRequest.getUsername(), newCart, "password");
        newCart.setUser(user);



        Item item= new Item();
        item.setId(1L);
        item.setName("Shoe");
        item.setPrice(new BigDecimal(200));


        Item item2= new Item();
        item2.setPrice(BigDecimal.valueOf(100));
        item2.setId(2L);
        item2.setName("Bag");

        //add two items in the cart
        newCart.addItem(item);
        newCart.addItem(item2);

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity response= cartController.removeFromcart(modifyCartRequest);
        Assert.assertEquals(200, response.getStatusCode().value());
        Cart cart= (Cart)response.getBody();
        Assert.assertEquals( BigDecimal.valueOf(100), cart.getTotal());
    }
}