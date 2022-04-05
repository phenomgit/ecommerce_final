package com.example.demo.controllerTest;

import com.example.demo.controllers.CartController;
import com.example.demo.testutils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class CartControllerTest {
    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private ModifyCartRequest modifyCartRequest;
    private User user;
    private Item item;
    private Cart cart;



    @Before // before each test case this object will be created 
    public void setUp() {
        cartController = new CartController();
        testutils.injectObjects(cartController, "userRepository", userRepository);
        testutils.injectObjects(cartController, "cartRepository", cartRepository);
        testutils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_to_cart_valid() throws Exception {

        User user;
        user = new User();


        user.setUsername("testingUsername ");
        user.setId(0);
        user.setPassword("testingUsername");
        Item item = new Item();
        item.setName("testingItem");
        item.setId((long) 0);
        item.setDescription("Description of item");
        item.setPrice(new BigDecimal(2));
       

        Cart cart;
        cart = new Cart();


        cart.setId((long) 0);
        user.setCart(cart);
        cart.setUser(user);

        modifyCartRequest = new ModifyCartRequest();


        modifyCartRequest.setItemId(0);

        modifyCartRequest.setUsername("testingUsername");

        modifyCartRequest.setQuantity(4);

        when(itemRepository.findById((long) 0)).thenReturn(java.util.Optional.of(item));
        when(userRepository.findByUsername("testingUsername")).thenReturn(user);
        
        List<Item> items_list = Arrays.asList(new Item[modifyCartRequest.getQuantity()]);


        int size = items_list.size();

        int i = 0;
        while (i < size) {
            items_list.set(i, item);
            i++;
        }

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);







        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());


        Cart actualCart = response.getBody();
        assertNotNull(actualCart);
        assertEquals(new BigDecimal(8), actualCart.getTotal());


        assertEquals(items_list, actualCart.getItems());
        assertEquals(cart.getUser(), actualCart.getUser());
        assertEquals(cart.getId(), actualCart.getId());
        
    }
}
