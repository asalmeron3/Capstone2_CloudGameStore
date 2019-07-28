package com.capstone2.gamestoreinvoiceservice.dao;

import com.capstone2.gamestoreinvoiceservice.model.InvoiceItem;

import java.util.List;

public interface InvoiceItemDao {
    List<InvoiceItem> getAllInvoiceItems();
    List<InvoiceItem> getAllInvoiceItemsByInvoiceId(int invoiceId);

    InvoiceItem createInvoiceItem(InvoiceItem invoiceItem);
    InvoiceItem getInvoiceItemById(int id);

    String updateInvoiceItem(InvoiceItem invoiceItem);
    String deleteInvoiceItem(int id);
}
