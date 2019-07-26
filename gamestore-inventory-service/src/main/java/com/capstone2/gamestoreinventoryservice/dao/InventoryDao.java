package com.capstone2.gamestoreinventoryservice.dao;

import com.capstone2.gamestoreinventoryservice.model.Inventory;

import java.util.List;

public interface InventoryDao {
    List<Inventory> getAllInventory();

    Inventory createInventory(Inventory inventory);
    Inventory getAnInventoryById(int inventoryId);

    String updateInventory(Inventory inventory);
    String deleteInventory(int inventoryId);
}
