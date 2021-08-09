package com.example.demo.controllerTests;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTests {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp () {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void testGetItems () {
        ResponseEntity<List<Item>> itemList = itemController.getItems();
        assertNotNull(itemList);
        assertEquals(200, itemList.getStatusCodeValue());
    }


    @Test
    public void testGetItemById () {
        Item item = new Item();
        item.setId(1l);
        when(itemRepository.findById(1l)).thenReturn(Optional.of(item));
        final ResponseEntity<Item> i = itemController.getItemById(1l);
        assertNotNull(i);
        assertEquals(200, i.getStatusCodeValue());
    }

    @Test
    public void testGetItemByInvalidId () {
        Item item = new Item();
        item.setId(1l);
        when(itemRepository.findById(2l)).thenReturn(Optional.of(item));
        final ResponseEntity<Item> i = itemController.getItemById(1l);
        assertNotNull(i);
        assertEquals(404, i.getStatusCodeValue());
    }

    @Test
    public void testGetItemsByName () {
        List<Item> itemList = new ArrayList();
        itemList.add(new Item());
        when(itemRepository.findByName("Square Widget")).thenReturn(itemList);
        final ResponseEntity<List<Item>> items = itemController.getItemsByName("Square Widget");
        assertNotNull(items);
        assertEquals(200, items.getStatusCodeValue());
        assertEquals(1, items.getBody().size());
    }

    @Test
    public void testGetItemsByInvalidName () {
        List<Item> itemList = new ArrayList();
        when(itemRepository.findByName("Invalid Widget")).thenReturn(itemList);
        final ResponseEntity<List<Item>> items = itemController.getItemsByName("Invalid Widget");
        assertNotNull(items);
        assertEquals(404, items.getStatusCodeValue());
    }

}
