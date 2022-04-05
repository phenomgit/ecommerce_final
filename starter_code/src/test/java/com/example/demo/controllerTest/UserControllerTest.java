package com.example.demo.controllerTest;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.testutils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before // before each test case this object will be created 
    public void setUp() {
        userController = new UserController();
        testutils.injectObjects(userController, "userRepository", userRepository);
        testutils.injectObjects(userController, "cartRepository", cartRepository);
        testutils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

        User user = new User();
        user.setId(0);
        user.setUsername("testUserName");
        user.setPassword("testPasswrd");

        Cart cart = new Cart();
        cart.setId((long) 0);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("testUserName")).thenReturn(user);
        when(userRepository.findById((long) 0)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("userDifferent")).thenReturn(null);
    }

    @Test
    public void findUserById() throws Exception {
        final ResponseEntity<User> response = userController.findById((long) 0);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        User actualUser = response.getBody();
        assertNotNull(actualUser);
        assertEquals(0, actualUser.getId());
        assertEquals("testPasswrd", actualUser.getPassword());
        assertEquals("testUserName", actualUser.getUsername());
    }

    @Test
    public void findUserByUsername() throws Exception {
        final ResponseEntity<User> response;
        response = userController.findByUserName("testUserName");


        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        User actualUser = response.getBody();

        assertNotNull(actualUser);
        assertEquals("testPasswrd", actualUser.getPassword());
        assertEquals(0, actualUser.getId());
        assertEquals("testUserName", actualUser.getUsername());


    }

    @Test
    public void createUserValidCredentials() throws Exception {

        when(bCryptPasswordEncoder.encode("testPasswrd"))
                .thenReturn("testHashPasswrd");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUserName");
        createUserRequest.setPassword("testPasswrd");
        createUserRequest.setConfirmPassword("testPasswrd");

        final ResponseEntity<User> response = userController
                .createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());


        User user;
        user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("testUserName", user.getUsername());
        assertEquals("testHashPasswrd", user.getPassword());
    }

    @Test
    public void createUserInvalidConfirmPassword() throws Exception {

        when(bCryptPasswordEncoder.encode("testPasswrd")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest;
        createUserRequest = new CreateUserRequest();


        createUserRequest.setUsername("testUserName");
        createUserRequest.setPassword("testPasswrd");
        createUserRequest.setConfirmPassword("wrongpassowrd");
        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400,response.getStatusCodeValue());

    }

    @Test
    public void createUserShortPassword() throws Exception {

        when(bCryptPasswordEncoder.encode("testPassword"))
                .thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest;
        createUserRequest = new CreateUserRequest();


        createUserRequest.setUsername("testUserName");
        createUserRequest.setPassword("pwd");
        createUserRequest.setConfirmPassword("pwd");
        final ResponseEntity<User> response;
        response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400,response.getStatusCodeValue());

    }

    @Test
    public void testUnavailableUser() {
        final ResponseEntity<User> response;
        response = userController.findByUserName("userDifferent");


        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
