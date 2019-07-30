package com.capstone2.gamestoreretailservice.feignClients;

import com.capstone2.gamestoreretailservice.model.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "inventory-service")
public interface InventoryClient {

    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.GET)
    Inventory getInventoryById(@PathVariable int id);

    @RequestMapping(value = "/inventory", method = RequestMethod.PUT)
    String updateInventory(@RequestBody Inventory inventory);

    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    List<Inventory> getAllInventories();

}
