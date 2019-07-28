package com.capstone2.gamestoreinvoiceservice.dao;

import com.capstone2.gamestoreinvoiceservice.model.InvoiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InvoiceItemDaoJdbcTemplateImpl implements InvoiceItemDao {

    @Autowired
    private JdbcTemplate invoiceItemJdbc;

    public InvoiceItemDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.invoiceItemJdbc =jdbcTemplate;
    }

    public InvoiceItem mapRowToItem(ResultSet rs, int rowNum) throws SQLException {
        InvoiceItem invoiceItem = new InvoiceItem(
                rs.getInt("invoice_id"),
                rs.getInt("inventory_id"),
                rs.getInt("quantity"),
                rs.getBigDecimal("unit_price")
        );
        invoiceItem.setInvoiceItemId(rs.getInt("invoice_item_id"));

        return invoiceItem;
    }

    private final static String CREATE_INVOICE_ITEM = "insert into invoice_item (invoice_id, inventory_id, quantity, " +
            "unit_price) values (?, ?, ?, ?)";
    private final static String GET_ALL_INVOICE_ITEMS = "select * from invoice_item";
    private final static String GET_INVOICE_ITEMS_BY_INVOICE_ID = "select * from invoice_item where invoice_id = ?";
    private final static String GET_INVOICE_ITEM = "select * from invoice_item where invoice_item_id = ?";
    private final static String UPDATE_INVOICE_ITEM = "update invoice_item set invoice_id = ?, inventory_id = ?, " +
            "quantity = ?, unit_price = ? where invoice_item_id = ?";
    private final static String DELETE_INVOICE_ITEM = "delete from invoice_item where invoice_item_id = ?";


    @Override
    public List<InvoiceItem> getAllInvoiceItems() {
        return invoiceItemJdbc.query(GET_ALL_INVOICE_ITEMS, this::mapRowToItem);
    }

    @Override
    public List<InvoiceItem> getAllInvoiceItemsByInvoiceId(int invoiceId) {
        return invoiceItemJdbc.query(GET_INVOICE_ITEMS_BY_INVOICE_ID, this::mapRowToItem, invoiceId);
    }

    @Override
    public InvoiceItem createInvoiceItem(InvoiceItem invoiceItem) {
        invoiceItemJdbc.update(
                CREATE_INVOICE_ITEM,
                invoiceItem.getInvoiceId(),
                invoiceItem.getInventoryId(),
                invoiceItem.getQuantity(),
                invoiceItem.getUnitPrice()
        );

        int id = invoiceItemJdbc.queryForObject("select LAST_INSERT_ID()", Integer.class);

        invoiceItem.setInvoiceItemId(id);
        return invoiceItem;
    }

    @Override
    public InvoiceItem getInvoiceItemById(int id) {
        try {
            return invoiceItemJdbc.queryForObject(GET_INVOICE_ITEM, this::mapRowToItem, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String updateInvoiceItem(InvoiceItem invoiceItem) {
        String failedMessage = "Unsuccessful: No matching invoiceItem was found";
        String successMessage = "Update successful: " + invoiceItem.toString();

        int rowsUpdate = invoiceItemJdbc.update(
                UPDATE_INVOICE_ITEM,
                invoiceItem.getInvoiceId(),
                invoiceItem.getInventoryId(),
                invoiceItem.getQuantity(),
                invoiceItem.getUnitPrice(),
                invoiceItem.getInvoiceItemId()
        );

        return rowsUpdate == 1 ? successMessage : failedMessage;
    }

    @Override
    public String deleteInvoiceItem(int invoiceItemId) {
        String successful = "Deletion successful. InvoiceItem with invoiceItemId '" +
                invoiceItemId + "' has been deleted";

        String unsuccessful = "Deletion NOT successful. No invoiceItem with invoiceItemId '" +
                invoiceItemId + "' was found.";

        int rowsDeleted = invoiceItemJdbc.update(DELETE_INVOICE_ITEM, invoiceItemId);

        return rowsDeleted == 1 ? successful : unsuccessful;
    }
}
