package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ItemControllerTest {
    private ItemRepository itemRepository= mock(ItemRepository.class);
    private ItemController itemController = new ItemController();

    @BeforeEach
    void setUp() {
        TestUtils.injectObject(itemController,"itemRepository", itemRepository);
    }

    @Test
    void getItems() {
        Item item= new Item();
        item.setId(1L);
        item.setName("Shoe");
        List<Item> items= new ArrayList<>();
        items.add(item);
        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity response= itemController.getItems();
        Assert.assertEquals(200, response.getStatusCode().value());
        List<Item> resValue= (List)response.getBody();
        Assert.assertEquals("Shoe",resValue.get(0).getName());
    }

    @Test
    void getItemById() {
        Item item= new Item();
        item.setId(1L);
        item.setName("Shoe");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity response= itemController.getItemById(1L);
        Assert.assertEquals(200, response.getStatusCode().value());
        Item resValue= (Item)response.getBody();
        Assert.assertEquals("Shoe",resValue.getName());
    }

    @Test
    void getItemsByName() {
        Item item= new Item();
        item.setId(1L);
        item.setName("Shoe");
        List<Item> items= new ArrayList<>();
        items.add(item);
        when(itemRepository.findByName("Shoe")).thenReturn(items);
        ResponseEntity response= itemController.getItemsByName("Shoe");
        Assert.assertEquals(200, response.getStatusCode().value());
        List<Item> resValue= (List)response.getBody();
        Assert.assertEquals("Shoe",resValue.get(0).getName());
    }
}