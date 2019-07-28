package com.capstone2.gamestoreinvoiceservice.controller;

import com.capstone2.gamestoreinvoiceservice.dao.InvoiceDaoJdbcTemplateImpl;
import com.capstone2.gamestoreinvoiceservice.exception.NotFoundException;
import com.capstone2.gamestoreinvoiceservice.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RefreshScope
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    InvoiceDaoJdbcTemplateImpl invoiceDao;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice addInvoice(@RequestBody @Valid Invoice inventory) {
        return invoiceDao.createInvoice(inventory);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> getAllInvoices() {return invoiceDao.getAllInvoices();}

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invoice getInvoiceById(@PathVariable int id) {
        return invoiceDao.getInvoiceById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String updateInvoice(@RequestBody @Valid Invoice inventory) {
        if(inventory.getInvoiceId() == 0) {
            throw new NotFoundException("An 'invoiceId' is required to update an invoice");
        }
        return invoiceDao.updateInvoice(inventory);}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteInvoice(@PathVariable int id) {
        try {
            return invoiceDao.deleteInvoice(id);
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException("This invoice cannot be delete because there are invoiceItems associated with" +
                    " it. To delete, please delete all associated invoiceItems first");
        }
    }
}
