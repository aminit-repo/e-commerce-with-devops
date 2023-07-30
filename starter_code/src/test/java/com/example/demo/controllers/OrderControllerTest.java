package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.People;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderControllerTest {

    private OrderController orderController= new OrderController();
    private UserRepository userRepository=  mock(UserRepository.class);
    private OrderRepository orderRepository= mock(OrderRepository.class);

    @BeforeEach
    void setUp() {
        TestUtils.injectObject(orderController,"userRepository",userRepository);
        TestUtils.injectObject(orderController,"orderRepository",orderRepository);
    }

    @Test
    void submit() {
        ModifyCartRequest modifyCartRequest= new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("JWT");
        modifyCartRequest.setQuantity(2);

        Cart newCart= new Cart();

        People user= new People(modifyCartRequest.getUsername(), newCart, "password");
        newCart.setUser(user);


        Item item= new Item();
        item.setId(1L);
        item.setName("Shoe");
        item.setPrice(new BigDecimal(200));
        newCart.addItem(item);
        when(userRepository.findByUsername("JWT")).thenReturn(user);
        ResponseEntity response= orderController.submit("JWT");

        Assert.assertEquals(200, response.getStatusCode().value());

    }
}