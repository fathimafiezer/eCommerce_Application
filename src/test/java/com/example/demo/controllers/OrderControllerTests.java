package com.example.demo.controllers;

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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTests {
    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);


    @Before
    public void setup() {
        User user = new User();
        user.setUsername("username");
        user.setCart(userCart(user));
        orderController = new OrderController(userRepository, orderRepository);
        when(userRepository.findByUsername("username")).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(userOrders());
    }

    @Test
    public void OrderSubmit_Success() {
        ResponseEntity<UserOrder> response = orderController.submit("username");
        UserOrder userOrder = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(userOrder);
        assertEquals(userOrder.getUser().getUsername(), "username");
        assertEquals(userOrder.getItems().size(), 1);
        assertEquals(userOrder.getTotal(), new BigDecimal(100));
    }

    @Test
    public void OrderSubmit_Error() {
        ResponseEntity<UserOrder> response = orderController.submit("");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser_Test() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("username");
        List<UserOrder> userOrders = response.getBody();
        assertEquals(200,response.getStatusCodeValue());
        assertNotNull(userOrders);
        assertEquals(userOrders.size(), 1);

        UserOrder userOrder = userOrders.get(0);
        assertEquals(userOrder.getUser().getUsername(), "username");
        assertEquals(userOrder.getTotal(), new BigDecimal(100));
        assertEquals(userOrder.getItems().size(), 1);
    }

    @Test
    public void getOrderForUser_Error() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("");
        assertEquals(404, response.getStatusCodeValue());
    }


    private static Cart userCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(getItem().orElse(null));
        return cart;
    }

    private static List<UserOrder> userOrders() {
        User user = new User();
        user.setUsername("username");
        user.setCart(userCart(user));
        UserOrder userOrder = UserOrder.createFromCart(user.getCart());
        List<UserOrder> userOrderList=new ArrayList<>();
        userOrderList.add(userOrder);
        return userOrderList;
    }

    private static Optional<Item> getItem() {
        Item item = new Item();
        item.setId(1l);
        item.setPrice(new BigDecimal(100));
        return Optional.of(item);
    }

}
