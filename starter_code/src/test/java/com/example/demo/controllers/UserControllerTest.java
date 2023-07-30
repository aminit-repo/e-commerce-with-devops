package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.People;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.JWTService;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private UserController userController= new UserController();
    private UserRepository userRepository= mock(UserRepository.class);
    private CartRepository cartRepository= mock(CartRepository.class);
    private OrderRepository orderRepository= mock(OrderRepository.class);
    private ItemRepository itemRepository= mock(ItemRepository.class);
    private BCryptPasswordEncoder passwordEncoder= mock(BCryptPasswordEncoder.class);


    private JWTService jwtService= mock(JWTService.class);

    private AuthenticationManager authenticationManager= mock(AuthenticationManager.class);
    @BeforeEach
    void setUp() {
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController,"cartRepository", cartRepository);
        TestUtils.injectObject(userController,"cartRepository", cartRepository);
        TestUtils.injectObject(userController,"passwordEncoder", passwordEncoder);
        TestUtils.injectObject(userController,"jwtService", jwtService);
        TestUtils.injectObject(userController,"authenticationManager", authenticationManager);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void findById() {
        CreateUserRequest createUserRequest= getUserRequest();
        People people=new People(createUserRequest.getUsername(), new Cart(), createUserRequest.getPassword());
        when(userRepository.findById(1L)).thenReturn(Optional.of(people));
        ResponseEntity response= userController.findById(1L);
        Assert.assertEquals(200, response.getStatusCode().value());
        People resp = (People)response.getBody();
        Assert.assertEquals(createUserRequest.getUsername(), resp.getUsername());
    }

    @Test
    void findByUserName() {
        CreateUserRequest createUserRequest= getUserRequest();
        People people=new People(createUserRequest.getUsername(), new Cart(), createUserRequest.getPassword());
        when(userRepository.findByUsername(createUserRequest.getUsername())).thenReturn(people);
        ResponseEntity response= userController.findByUserName(createUserRequest.getUsername());
        Assert.assertEquals(200, response.getStatusCode().value());
        People resp = (People)response.getBody();
        Assert.assertEquals(createUserRequest.getUsername(), resp.getUsername());
    }

    @Test
    void createUser() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(new People())).thenReturn(new People("JWT", new Cart(), "password"));
        CreateUserRequest createUserRequest= getUserRequest();
        People user= new People(createUserRequest.getUsername(), new Cart(),createUserRequest.getPassword());
        when(userRepository.save(user)).thenReturn(user);
        ResponseEntity<People> response= userController.createUser(createUserRequest);

        Assert.assertNotNull(response);
        People users= response.getBody();
        Assert.assertNotNull(users);
        Assert.assertEquals(200, response.getStatusCode().value());
        Assert.assertEquals("encodedPassword", users.getPassword());
    }

    @Test
    void signin() {
        CreateUserRequest createUserRequest= getUserRequest();
        Authentication authentication=new UsernamePasswordAuthenticationToken("JWT","password",new ArrayList<>());
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(createUserRequest.getUsername(), createUserRequest.getPassword()))).thenReturn(authentication);
        when(jwtService.getJWTString(authentication)).thenReturn("thisIsAToken");

        //login into system
        ResponseEntity resp= userController.signin(createUserRequest);
        Assert.assertEquals(200, resp.getStatusCode().value());
        Assert.assertEquals("thisIsAToken", resp.getHeaders().get("Authorization").get(0));
    }




    public static  CreateUserRequest getUserRequest(){
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setPassword("password");
        createUserRequest.setUsername("JWT");
        createUserRequest.setConfirmPassword("password");
        return createUserRequest;
    }


}