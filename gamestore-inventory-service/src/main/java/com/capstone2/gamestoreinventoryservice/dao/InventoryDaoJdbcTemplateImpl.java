package com.capstone2.gamestoreinventoryservice.dao;

import com.capstone2.gamestoreinventoryservice.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InventoryDaoJdbcTemplateImpl implements InventoryDao {

    @Autowired
    JdbcTemplate inventoryJdbcTemplate;

    public InventoryDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.inventoryJdbcTemplate = jdbcTemplate;
    }

    public Inventory mapRowToInventory(ResultSet rs, int rowNum) throws SQLException {
        Inventory inventory = new Inventory();

        inventory.setProductId(rs.getInt("product_id"));
        inventory.setQuantity(rs.getInt("quantity"));
        inventory.setInventoryId(rs.getInt("inventory_id"));

        return inventory;
    }

    private final static String GET_ALL_INVENTORIES = "select * from inventory";
    private final static String GET_INVENTORY_BY_ID = "select * from inventory where inventory_id = ?";
    private final static String CREATE_INVENTORY = "insert into inventory (product_id, quantity) values (?, ?)";
    private final static String DELETE_INVENTORY = "delete from inventory where inventory_id = ?";
    private final static String UPDATE_INVENTORY = "update inventory set product_id = ?, quantity = ? where " +
            "inventory_id = ?";

    @Override
    public List<Inventory> getAllInventory() {
        return inventoryJdbcTemplate.query(GET_ALL_INVENTORIES, this::mapRowToInventory);
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        inventoryJdbcTemplate.update(CREATE_INVENTORY, inventory.getProductId(), inventory.getQuantity());

        int inventoryId = inventoryJdbcTemplate.queryForObject("select LAST_INSERT_ID()", Integer.class);
        inventory.setInventoryId(inventoryId);

        return inventory;
    }

    @Override
    public Inventory getAnInventoryById(int inventoryId) {
        try {
            return inventoryJdbcTemplate.queryForObject(GET_INVENTORY_BY_ID, this::mapRowToInventory, inventoryId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String updateInventory(Inventory inventory) {
        String failedMessage = "Unsuccessful: No matching inventory was found";
        String successMessage = "Update successful: " + inventory.toString();

        int rowsUpdated = inventoryJdbcTemplate.update(
                UPDATE_INVENTORY,
                inventory.getProductId(),
                inventory.getQuantity(),
                inventory.getInventoryId()
        );

        return rowsUpdated == 1 ? successMessage : failedMessage;
    }

    @Override
    public String deleteInventory(int inventoryId) {
        String successful = "Deletion successful. Inventory with inventoryId '" + inventoryId + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No inventory with inventoryId '" + inventoryId + "' was found.";

        int rowsDeleted = inventoryJdbcTemplate.update(DELETE_INVENTORY, inventoryId);

        return rowsDeleted == 1 ? successful : unsuccessful;

    }
}
