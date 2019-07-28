package com.capstone2.gamestoreinvoiceservice.dao;

import com.capstone2.gamestoreinvoiceservice.model.Invoice;

import java.util.List;

public interface InvoiceDao {

    List<Invoice> getAllInvoices();

    Invoice createInvoice(Invoice invoice);
    Invoice getInvoiceById(int id);

    String updateInvoice(Invoice invoice);
    String deleteInvoice(int id);

}
