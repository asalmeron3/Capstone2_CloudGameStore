package com.capstone2.gamestoreinventoryservice.controller;

import com.capstone2.gamestoreinventoryservice.dao.InventoryDaoJdbcTemplateImpl;
import com.capstone2.gamestoreinventoryservice.model.Inventory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryDaoJdbcTemplateImpl inventoryDao;

    private ObjectMapper om = new ObjectMapper();
    private Inventory inventoryExpected;

    @Before
    public void setUp() {
        inventoryExpected = new Inventory(9, 12);
        inventoryExpected.setInventoryId(1);
    }

    @Test
    public void testPostInventory() throws Exception {

        Inventory inventoryInfo = new Inventory(9, 12);

        Inventory inventoryNoProductId = new Inventory();
        inventoryNoProductId.setQuantity(7);

        Inventory inventoryNoQuantity = new Inventory();
        inventoryNoQuantity.setProductId(4);

        Mockito.when(inventoryDao.createInventory(inventoryInfo)).thenReturn(inventoryExpected);

        String expectedInventoryJson = om.writeValueAsString(inventoryExpected);
        String inventoryInfoJson = om.writeValueAsString(inventoryInfo);
        String inventoryNoProductIdJson = om.writeValueAsString(inventoryNoProductId);
        String inventoryNoQuantityJson = om.writeValueAsString(inventoryNoQuantity);


        mockMvc.perform(MockMvcRequestBuilders.post("/inventory")
                .content(inventoryInfoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedInventoryJson));

        mockMvc.perform(MockMvcRequestBuilders.post("/inventory")
                .content(inventoryNoProductIdJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(MockMvcRequestBuilders.post("/inventory")
                .content(inventoryNoQuantityJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void testGetInventory() throws Exception {

        Mockito.when(inventoryDao.getAnInventoryById(0)).thenReturn(null);
        Mockito.when(inventoryDao.getAnInventoryById(1)).thenReturn(inventoryExpected);

        String expectedInventoryJson = om.writeValueAsString(inventoryExpected);

        mockMvc.perform(MockMvcRequestBuilders.get("/inventory/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedInventoryJson));

        mockMvc.perform(MockMvcRequestBuilders.get("/inventory/0"))
                .andDo(print())
                .andExpect(content().string(""));
    }

    @Test
    public void testGetAllInventorys() throws Exception {
        Inventory inventoryInfo1 = new Inventory(4, 5);
        inventoryInfo1.setInventoryId(2);

        Inventory inventoryInfo2 = new Inventory(7, 2);
        inventoryInfo2.setInventoryId(3);

        List<Inventory> fullInventory = new ArrayList<>();
        fullInventory.add(inventoryInfo1);
        fullInventory.add(inventoryInfo2);

        String fullInventoryJson = om.writeValueAsString(fullInventory);

        Mockito.when(inventoryDao.getAllInventory()).thenReturn(fullInventory);

        mockMvc.perform(MockMvcRequestBuilders.get("/inventory"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(fullInventoryJson));
    }

    @Test
    public void deleteInventory() throws Exception {

        String successful = "Deletion successful. Inventory with inventoryId '" + 1 + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No inventory with inventoryId '" + 0 + "' was found.";

        Mockito.when(inventoryDao.deleteInventory(0)).thenReturn(unsuccessful);
        Mockito.when(inventoryDao.deleteInventory(1)).thenReturn(successful);

        mockMvc.perform(MockMvcRequestBuilders.delete("/inventory/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successful));

        mockMvc.perform(MockMvcRequestBuilders.delete("/inventory/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(unsuccessful));

    }

    @Test
    public void updateInventory() throws Exception {
        Inventory inventoryId0NotAllowed = new Inventory(5, 9);
        inventoryId0NotAllowed.setInventoryId(0);

        Inventory inventoryId4NotFound = new Inventory(10, 21);
        inventoryId4NotFound.setInventoryId(4);

        String inventoryId0NotAllowedJson = om.writeValueAsString(inventoryId0NotAllowed);
        String inventory4NotFoundJson = om.writeValueAsString(inventoryId4NotFound);
        String inventoryExpectedJson = om.writeValueAsString(inventoryExpected);

        String successful = "Update successful: "+ inventoryExpected.toString();
        String unsuccessful = "Update NOT successful. Please try again.";

        Mockito.when(inventoryDao.updateInventory(inventoryId4NotFound)).thenReturn(unsuccessful);
        Mockito.when(inventoryDao.updateInventory(inventoryExpected)).thenReturn(successful);

        mockMvc.perform(MockMvcRequestBuilders.put("/inventory")
                .content(inventoryExpectedJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successful));

        mockMvc.perform(MockMvcRequestBuilders.put("/inventory")
                .content(inventory4NotFoundJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(unsuccessful));

        mockMvc.perform(MockMvcRequestBuilders.put("/inventory")
                .content(inventoryId0NotAllowedJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnprocessableEntity());
    }
}
