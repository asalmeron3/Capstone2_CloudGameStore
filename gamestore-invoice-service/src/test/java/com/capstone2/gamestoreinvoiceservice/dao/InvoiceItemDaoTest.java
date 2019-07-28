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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceItemDaoTest {
    @Autowired
    private InvoiceItemDao invoiceItemDao;

    @Autowired
    private InvoiceDao invoiceDao;

    private int invoiceId;

    @Before
    public void setup() {
        List<InvoiceItem> allInvoiceItem = invoiceItemDao.getAllInvoiceItems();

        allInvoiceItem.stream().forEach(
                invoiceItem -> invoiceItemDao.deleteInvoiceItem(invoiceItem.getInvoiceItemId())
        );

        Invoice invoiceSample = new Invoice(4, LocalDate.of(2019, 07, 26));
        invoiceId = invoiceDao.createInvoice(invoiceSample).getInvoiceId();
    }

    @Test
    public void createGetDeleteInvoiceItem() {

        InvoiceItem invoiceItemExpected = new InvoiceItem(invoiceId,2,3, new BigDecimal(55.30));

        InvoiceItem invoiceItemToAdd = new InvoiceItem(invoiceId,2,3, new BigDecimal(55.30));

        InvoiceItem invoiceItemAdded = invoiceItemDao.createInvoiceItem(invoiceItemToAdd);

        int id = invoiceItemAdded.getInvoiceItemId();
        invoiceItemExpected.setInvoiceItemId(id);

        Assert.assertEquals(invoiceItemExpected, invoiceItemAdded);

        InvoiceItem invoiceItemFound = invoiceItemDao.getInvoiceItemById(id);
        Assert.assertEquals(invoiceItemExpected, invoiceItemFound);

        String successful = "Deletion successful. InvoiceItem with invoiceItemId '" + id + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No invoiceItem with invoiceItemId '" + 0 + "' was found.";

        String deleteSuccessMsg = invoiceItemDao.deleteInvoiceItem(id);
        String deleteNotSuccessfulMsg = invoiceItemDao.deleteInvoiceItem(0);

        Assert.assertEquals(successful, deleteSuccessMsg);
        Assert.assertEquals(unsuccessful, deleteNotSuccessfulMsg);
    }

    @Test
    public void updatedInvoiceItem() {
        InvoiceItem invoiceItemToFailUpdate = new InvoiceItem(invoiceId,5,3, new BigDecimal(1.40));

        InvoiceItem invoiceItemToAdd = new InvoiceItem(invoiceId,2,3, new BigDecimal(55.30));

        InvoiceItem invoiceItemToUpdate = invoiceItemDao.createInvoiceItem(invoiceItemToAdd);
        invoiceItemToUpdate.setQuantity(8);

        String failedMessage = "Unsuccessful: No matching invoiceItem was found";
        String successMessage = "Update successful: " + invoiceItemToUpdate.toString();

        String updateFailedMsg = invoiceItemDao.updateInvoiceItem(invoiceItemToFailUpdate);
        String updateSucceededMsg = invoiceItemDao.updateInvoiceItem(invoiceItemToUpdate);

        Assert.assertEquals(failedMessage, updateFailedMsg);
        Assert.assertEquals(successMessage, updateSucceededMsg);
    }

    @Test
    public void getAllInvoiceItem() {
        Invoice invoiceSample2 = new Invoice(4, LocalDate.of(2019, 07, 26));
        int invoiceId2 = invoiceDao.createInvoice(invoiceSample2).getInvoiceId();

        InvoiceItem invoiceItem1 = new InvoiceItem(invoiceId, 5, 8, new BigDecimal(2.60));
        InvoiceItem invoiceItem2 = new InvoiceItem(invoiceId2, 6, 3, new BigDecimal(1.40));

        invoiceItem1 = invoiceItemDao.createInvoiceItem(invoiceItem1);
        invoiceItem2 = invoiceItemDao.createInvoiceItem(invoiceItem2);

        List<InvoiceItem> allInvoiceItem = new ArrayList<>();
        allInvoiceItem.add(invoiceItem1);
        allInvoiceItem.add(invoiceItem2);

        Assert.assertEquals(2, invoiceItemDao.getAllInvoiceItems().size());

        Assert.assertEquals(1, invoiceItemDao.getAllInvoiceItemsByInvoiceId(invoiceId).size());
        Assert.assertEquals(1, invoiceItemDao.getAllInvoiceItemsByInvoiceId(invoiceId2).size());


    }
}
