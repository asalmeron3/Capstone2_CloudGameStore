package com.capstone2.gamestoreretailservice.feignClients;

import com.capstone2.gamestoreretailservice.model.Invoice;
import com.capstone2.gamestoreretailservice.model.InvoiceItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "invoice-service")
public interface InvoiceCleint {

    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    Invoice createAnInvoice(@RequestBody @Valid Invoice invoice);

    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.GET)
    Invoice getInvoiceById(@PathVariable int id);

    @RequestMapping(value = "/invoices/customer/{id}", method = RequestMethod.GET)
    List<Invoice> getInvoicesByCustomerId(@PathVariable int id);

    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    List<Invoice> getAllInvoices();

    @RequestMapping(value = "/invoiceItem", method = RequestMethod.POST)
    InvoiceItem createInvoiceItem(@RequestBody @Valid InvoiceItem invoiceItem);

    @RequestMapping(value = "/invoiceItems/{id}", method = RequestMethod.GET)
    InvoiceItem getInvoiceItemById(@PathVariable int id);

    @RequestMapping(value = "/invoiceItems/invoice/{id}", method = RequestMethod.GET)
    List<InvoiceItem> getInvoiceItemsByInvoiceId(@PathVariable int id);



}
