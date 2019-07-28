package com.capstone2.gamestoreinvoiceservice.dao;

import com.capstone2.gamestoreinvoiceservice.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InvoiceDaoJdbcTemplateImpl implements InvoiceDao {

    @Autowired
    private JdbcTemplate invoiceJdbc;

    public InvoiceDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.invoiceJdbc = jdbcTemplate;
    }

    public Invoice mapRowToInvoice(ResultSet rs, int rowNum) throws SQLException {
        Invoice invoice = new Invoice(
                rs.getInt("customer_id"),
                rs.getDate("purchase_date").toLocalDate());
        invoice.setInvoiceId(rs.getInt("invoice_id"));

        return invoice;
    }

    private final static String CREATE_INVOICE = "insert into invoice (customer_id, purchase_date) values (?, ?)";
    private final static String GET_ALL_INVOICES = "select * from invoice";
    private final static String GET_INVOICE = "select * from invoice where invoice_id = ?";
    private final static String UPDATE_INVOICE =
            "update invoice set customer_id = ?, purchase_date = ? where invoice_id = ?";
    private final static String DELETE_INVOICE = "delete from invoice where invoice_id = ?";

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceJdbc.query(GET_ALL_INVOICES, this::mapRowToInvoice);
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        invoiceJdbc.update(CREATE_INVOICE, invoice.getCustomerId(), invoice.getPurchaseDate());

        int id = invoiceJdbc.queryForObject("select LAST_INSERT_ID()", Integer.class);

        invoice.setInvoiceId(id);
        return invoice;
    }

    @Override
    public Invoice getInvoiceById(int id) {
        try {
            return invoiceJdbc.queryForObject(GET_INVOICE, this::mapRowToInvoice, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String updateInvoice(Invoice invoice) {
        String failedMessage = "Unsuccessful: No matching invoice was found";
        String successMessage = "Update successful: " + invoice.toString();

        int rowsUpdate = invoiceJdbc.update(
                UPDATE_INVOICE,
                invoice.getInvoiceId(),
                invoice.getPurchaseDate(),
                invoice.getInvoiceId());

        return rowsUpdate == 1 ? successMessage : failedMessage;
    }

    @Override
    public String deleteInvoice(int invoiceId) {
        String successful = "Deletion successful. Invoice with invoiceId '" + invoiceId + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No invoice with invoiceId '" + invoiceId + "' was found.";

        int rowsDeleted = invoiceJdbc.update(DELETE_INVOICE, invoiceId);

        return rowsDeleted == 1 ? successful : unsuccessful;
    }
}
