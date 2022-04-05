package com.example.demo.controllerTest;

import com.example.demo.model.persistence.User;
import com.example.demo.testutils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private Item item;
    private User user;
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before  // before each test case this object will be created 
    public void setUp() {
        itemController = new ItemController();
        testutils.injectObjects(itemController, "itemRepository", itemRepository);

        item = new Item();
        
        
        item.setName("testItem");
        item.setPrice(new BigDecimal(1.23));
        item.setDescription("Description of a test item");
        Optional<Item> optionalItem = Optional.of(item);

        List<Item> item_list = new ArrayList<>();
        int i = 0;
        while (i < 3) {
            item.setId((long) i);
            item_list.add(item);
            i++;
        }
        when(itemRepository.findAll()).thenReturn(item_list);
        when(itemRepository.findByName("testItem")).thenReturn(item_list);
        when(itemRepository.findById((long) 0)).thenReturn(optionalItem);
       
        when(itemRepository.findByName("testItemNull")).thenReturn(null);
        when(itemRepository.findByName("testItemDifferent")).thenReturn(item_list);

    }

    @Test
    public void get_items_valid() throws Exception {

        final ResponseEntity<List<Item>> response;
        response = itemController.getItems();


        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());


        List<Item> testItems = response.getBody();
        assertNotNull(testItems);

        int i=0;
        while (i < 3) {
            assertTrue(testItems.contains(item));
            i++;
        }

    }

    @Test
    public void get_item_byID_valid() throws Exception {

        final ResponseEntity<Item> response = itemController.getItemById((long) 0);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        Item testItems = response.getBody();



        assertNotNull(testItems);
        assertEquals(item.getId(), testItems.getId());
        assertEquals(item.getName(), testItems.getName());
    }

    @Test
    public void test_item_byName_null() throws Exception {
        final ResponseEntity<List<Item>> response;
        response = itemController.getItemsByName("testItemNull");


        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void get_items_by_name_invalid() throws Exception {

        final ResponseEntity<List<Item>> response;
        response = itemController.getItemsByName("testItemDifferent");


        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        List<Item> itemsByNameBody = response.getBody();

        assertNotNull(itemsByNameBody);
        assertTrue(itemsByNameBody.contains(item));
    }



}