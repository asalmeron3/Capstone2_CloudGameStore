package com.capstone2.gamestoreinventoryservice.dao;

import com.capstone2.gamestoreinventoryservice.model.Inventory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InventoryDaoJdbcTemplateTest {

    @Autowired
    private InventoryDao inventoryDao;

    @Before
    public void setup() {
        List<Inventory> allInventory = inventoryDao.getAllInventory();
        allInventory.stream().forEach(inventory -> inventoryDao.deleteInventory(inventory.getInventoryId()));
    }

    @Test
    public void createGetDeleteInventory() {
        Inventory inventoryExpected = new Inventory(4,1);

        Inventory inventoryToAdd = new Inventory(4,1);

        Inventory inventoryAdded = inventoryDao.createInventory(inventoryToAdd);

        int id = inventoryAdded.getInventoryId();
        inventoryExpected.setInventoryId(id);

        Assert.assertEquals(inventoryExpected, inventoryAdded);

        Inventory inventoryFound = inventoryDao.getAnInventoryById(id);
        Assert.assertEquals(inventoryExpected, inventoryFound);

        String successful = "Deletion successful. Inventory with inventoryId '" + id + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No inventory with inventoryId '" + 0 + "' was found.";

        String deleteSuccessMsg = inventoryDao.deleteInventory(id);
        String deleteNotSuccessfulMsg = inventoryDao.deleteInventory(0);

        Assert.assertEquals(successful, deleteSuccessMsg);
        Assert.assertEquals(unsuccessful, deleteNotSuccessfulMsg);
    }

    @Test
    public void updatedInventory() {
        Inventory inventoryToFailUpdate = new Inventory(5, 2);
        Inventory inventoryToUpdate = new Inventory(4,1);

        Inventory inventoryAdded = inventoryDao.createInventory(inventoryToUpdate);
        System.out.println(inventoryAdded.toString());
        int id = inventoryAdded.getInventoryId();
        inventoryToUpdate.setInventoryId(id);
        inventoryToUpdate.setQuantity(8);

        String failedMessage = "Unsuccessful: No matching inventory was found";
        String successMessage = "Update successful: " + inventoryToUpdate.toString();

        String updateFailedMsg = inventoryDao.updateInventory(inventoryToFailUpdate);
        String updateSucceededMsg = inventoryDao.updateInventory(inventoryToUpdate);

        Assert.assertEquals(failedMessage, updateFailedMsg);
        Assert.assertEquals(successMessage, updateSucceededMsg);
    }

    @Test
    public void getAllInventory() {
        Inventory inventory1 = new Inventory(5, 2);
        Inventory inventory2 = new Inventory(4,1);

        inventory1 = inventoryDao.createInventory(inventory1);
        inventory2 = inventoryDao.createInventory(inventory2);

        List<Inventory> allInventory = new ArrayList<>();
        allInventory.add(inventory1);
        allInventory.add(inventory2);

        Assert.assertEquals(2, inventoryDao.getAllInventory().size());
    }
}
