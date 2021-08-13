package com.example.demo.controllers;

import com.example.demo.TestUtils;
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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTests {

    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);


    @Before
    public void setUp () {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
    }

    @Test
    public void testAddToCart() throws Exception{
        User user = new User();
        user.setId(1);
        user.setUsername("fathima");
        user.setPassword("password");
        user.setCart(new Cart());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        Optional<Item> itemOptional = Optional.of(new Item());
        itemOptional.get().setPrice(new BigDecimal(100.0));
        when(itemRepository.findById(any())).thenReturn(itemOptional);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(user.getUsername());
        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(response.getBody().getItems().size(), modifyCartRequest.getQuantity());
        assertEquals(response.getBody().getTotal(), itemOptional.get().getPrice());

    }

    @Test
    public void testAddToCartError() {
        User user = new User();
        user.setId(1);
        user.setUsername("fathima");
        user.setPassword("password");
        user.setCart(new Cart());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setItemId(0l);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void testRemoveFromCart() {
        User user = new User();
        user.setId(1);
        user.setUsername("fathima");
        user.setPassword("password");
        user.setCart(new Cart());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        Optional<Item> itemOptional = Optional.of(new Item());
        itemOptional.get().setPrice(new BigDecimal(100.0));
        when(itemRepository.findById(any())).thenReturn(itemOptional);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(200,response.getStatusCodeValue());
        assertNotNull(response);
        assertEquals(response.getBody().getItems().size(), 0);
    }

    @Test
    public void testInvalidUsername() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("---");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);
        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(404, response.getStatusCodeValue());
    }

}
