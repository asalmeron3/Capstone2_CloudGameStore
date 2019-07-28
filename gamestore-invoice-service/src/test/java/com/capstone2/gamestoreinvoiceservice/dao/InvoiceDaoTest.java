package com.capstone2.gamestoreinvoiceservice.dao;

import com.capstone2.gamestoreinvoiceservice.model.Invoice;
import com.capstone2.gamestoreinvoiceservice.model.InvoiceItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceDaoTest {
    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private InvoiceItemDao invoiceItemDao;

    private Invoice invoiceSample;

    @Before
    public void setup() {
        List<InvoiceItem> allInvoiceItems = invoiceItemDao.getAllInvoiceItems();
        allInvoiceItems.stream().forEach(item -> invoiceItemDao.deleteInvoiceItem(item.getInvoiceItemId()));

        List<Invoice> allInvoice = invoiceDao.getAllInvoices();
        allInvoice.stream().forEach(invoice -> invoiceDao.deleteInvoice(invoice.getInvoiceId()));

        invoiceSample = new Invoice(4,LocalDate.of(2019, 07, 26));
        invoiceSample = invoiceDao.createInvoice(invoiceSample);
    }

    @Test
    public void createGetInvoice() {
        Invoice invoiceExpected = new Invoice(4, LocalDate.of(2019, 07, 26));

        Invoice invoiceToAdd = new Invoice(4,LocalDate.of(2019, 07, 26));

        Invoice invoiceAdded = invoiceDao.createInvoice(invoiceToAdd);

        int id = invoiceAdded.getInvoiceId();
        invoiceExpected.setInvoiceId(id);

        Assert.assertEquals(invoiceExpected, invoiceAdded);

        Invoice invoiceFound = invoiceDao.getInvoiceById(id);
        Assert.assertEquals(invoiceExpected, invoiceFound);
    }

    @Test
    public void deleteInvoice() {
        int id = invoiceSample.getInvoiceId();

        String successful = "Deletion successful. Invoice with invoiceId '" + id + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No invoice with invoiceId '" + 0 + "' was found.";

        String deleteSuccessMsg = invoiceDao.deleteInvoice(id);
        String deleteNotSuccessfulMsg = invoiceDao.deleteInvoice(0);

        Assert.assertEquals(successful, deleteSuccessMsg);
        Assert.assertEquals(unsuccessful, deleteNotSuccessfulMsg);
    }

    @Test
    public void updatedInvoice() {
        Invoice invoiceToFailUpdate = new Invoice(5, LocalDate.of(2019, 07, 26));
        Invoice invoiceToUpdate = new Invoice(4,LocalDate.of(2019, 07, 26));

        Invoice invoiceAdded = invoiceDao.createInvoice(invoiceToUpdate);

        int id = invoiceAdded.getInvoiceId();
        invoiceToUpdate.setInvoiceId(id);
        invoiceToUpdate.setPurchaseDate(LocalDate.of(2019, 07, 27));

        String failedMessage = "Unsuccessful: No matching invoice was found";
        String successMessage = "Update successful: " + invoiceToUpdate.toString();

        String updateFailedMsg = invoiceDao.updateInvoice(invoiceToFailUpdate);
        String updateSucceededMsg = invoiceDao.updateInvoice(invoiceToUpdate);

        Assert.assertEquals(failedMessage, updateFailedMsg);
        Assert.assertEquals(successMessage, updateSucceededMsg);
    }

    @Test
    public void getAllInvoice() {
        Invoice invoice1 = new Invoice(5, LocalDate.of(2019, 07, 26));
        Invoice invoice2 = new Invoice(4, LocalDate.of(2019, 07, 26));

        invoice1 = invoiceDao.createInvoice(invoice1);
        invoice2 = invoiceDao.createInvoice(invoice2);

        List<Invoice> allInvoice = new ArrayList<>();
        allInvoice.add(invoice1);
        allInvoice.add(invoice2);

        // One added @Before test; 2 within test
        Assert.assertEquals(3, invoiceDao.getAllInvoices().size());
    }
}
