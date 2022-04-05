package com.example.demo.controllerTest;

import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.testutils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private User user;
    private Cart cart;
    private Item item;

    @Before  // before each test case this object will be created
    public void setUp() {
        orderController = new OrderController();
        testutils.injectObjects(orderController, "userRepository", userRepository);
        testutils.injectObjects(orderController, "orderRepository", orderRepository);

        user = new User();
        user.setId(0);
        user.setUsername("usernametest");
        user.setPassword("passwordtest");

        cart = new Cart();
        cart.setId((long) 0);
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(14.97));
        user.setCart(cart);

        item = new Item();
        item.setId((long) 0);
        item.setName("itemfortest");
        item.setPrice(new BigDecimal(5.99));
        item.setDescription("Description for test item");
        List<Item> itemsArray = new ArrayList<>();
        for (int i=0; i < 3; i++) {
            itemsArray.add(item);
        }
        cart.setItems(itemsArray);

        when(userRepository.findByUsername("usernametest")).thenReturn(user);
        when(userRepository.findByUsername("nullusername")).thenReturn(null);

    }

    @Test
    public void Place_Order_valid() throws Exception {

        assertNotNull(user);
        assertEquals("usernametest", user.getUsername());
        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder user_order = response.getBody();
        assertNotNull(user_order);
        assertEquals(cart.getTotal(), user_order.getTotal());
        assertEquals(cart.getUser(), user_order.getUser());
        assertEquals(cart.getItems(), user_order.getItems());

    }

    @Test
    public void getOrdersForUser_UnavailableUser() throws Exception {

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("nullusername");
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void submit_order_UnavailableUser() throws Exception {


        final ResponseEntity<UserOrder> response = orderController.submit("nullusername");
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }



}
