package com.capstone2.gamestoreinventoryservice.controller;

import com.capstone2.gamestoreinventoryservice.dao.InventoryDaoJdbcTemplateImpl;
import com.capstone2.gamestoreinventoryservice.exception.NotFoundException;
import com.capstone2.gamestoreinventoryservice.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    InventoryDaoJdbcTemplateImpl inventoryDao;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Inventory addInventory(@RequestBody @Valid Inventory inventory) {
        return inventoryDao.createInventory(inventory);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> getAllInventorys() {return inventoryDao.getAllInventory();}

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Inventory getInventoryById(@PathVariable int id) {
        return inventoryDao.getAnInventoryById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String updateInventory(@RequestBody @Valid Inventory inventory) {
        if(inventory.getInventoryId() == 0) {
            throw new NotFoundException("An 'inventoryId' is required to update an inventory");
        }
        return inventoryDao.updateInventory(inventory);}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteInventory(@PathVariable int id) {return inventoryDao.deleteInventory(id);}
}
