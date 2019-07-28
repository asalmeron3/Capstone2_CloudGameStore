package com.capstone2.gamestoreinvoiceservice.controller;

import com.capstone2.gamestoreinvoiceservice.dao.InvoiceItemDaoJdbcTemplateImpl;
import com.capstone2.gamestoreinvoiceservice.exception.NotFoundException;
import com.capstone2.gamestoreinvoiceservice.model.InvoiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RefreshScope
@RestController
@RequestMapping("/invoiceItems")
public class InvoiceItemController {

    @Autowired
    InvoiceItemDaoJdbcTemplateImpl invoiceItemDao;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceItem addInvoiceItem(@RequestBody @Valid InvoiceItem inventory) {
        try {
            return invoiceItemDao.createInvoiceItem(inventory);
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException("The invoiceId provided is not associated with an existing invoice");
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceItem> getAllInvoiceItems() {return invoiceItemDao.getAllInvoiceItems();}

    @GetMapping("/invoice/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceItem> getAllInvoiceItemsByInvoiceId(@PathVariable int id) {
        return invoiceItemDao.getAllInvoiceItemsByInvoiceId(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceItem getInvoiceItemById(@PathVariable int id) {
        return invoiceItemDao.getInvoiceItemById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String updateInvoiceItem(@RequestBody @Valid InvoiceItem inventory) {
        if (inventory.getInvoiceItemId() == 0) {
            throw new NotFoundException("An 'invoiceItemId' is required to update an invoiceItem");
        }
        try {
            return invoiceItemDao.updateInvoiceItem(inventory);
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException("The invoiceId is not associated with an existing invoice");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteInvoiceItem(@PathVariable int id) {return invoiceItemDao.deleteInvoiceItem(id);}
}
