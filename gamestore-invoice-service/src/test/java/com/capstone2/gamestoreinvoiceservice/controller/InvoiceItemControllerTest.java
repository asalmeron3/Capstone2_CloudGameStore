package com.capstone2.gamestoreinvoiceservice.controller;

import com.capstone2.gamestoreinvoiceservice.dao.InvoiceDaoJdbcTemplateImpl;
import com.capstone2.gamestoreinvoiceservice.dao.InvoiceItemDaoJdbcTemplateImpl;
import com.capstone2.gamestoreinvoiceservice.model.Invoice;
import com.capstone2.gamestoreinvoiceservice.model.InvoiceItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@RunWith(SpringRunner.class)
@WebMvcTest
public class InvoiceItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceItemDaoJdbcTemplateImpl itemDao;

    @MockBean
    private InvoiceDaoJdbcTemplateImpl invoiceDao;

    private ObjectMapper om = new ObjectMapper();
    private InvoiceItem expectedInvoiceItem;
    private Invoice invoice;

    @Before
    public void setUp() {
        expectedInvoiceItem = new InvoiceItem(9,8, 2,
                new BigDecimal(12.00).setScale(2, RoundingMode.FLOOR));
        expectedInvoiceItem.setInvoiceItemId(1);

        invoice = new Invoice(9, LocalDate.of(2017, 06, 14));
        invoice.setInvoiceId(9);
    }

    @Test
    public void testPostInvoiceItem() throws Exception {

        // invoiceId is 9 as set in @Before
        InvoiceItem invoiceItem = new InvoiceItem(9,8, 2,
                new BigDecimal(12.00).setScale(2, RoundingMode.FLOOR));

        InvoiceItem invoiceItemMissingAProperty = new InvoiceItem();
        invoiceItemMissingAProperty.setInvoiceItemId(9);

        Mockito.when(itemDao.createInvoiceItem(invoiceItem)).thenReturn(expectedInvoiceItem);

        String expectedInvoiceItemJson = om.writeValueAsString(expectedInvoiceItem);
        String invoiceItemJson = om.writeValueAsString(invoiceItem);
        String invoiceItemMissingAPropertyJson = om.writeValueAsString(invoiceItemMissingAProperty);


        mockMvc.perform(MockMvcRequestBuilders.post("/invoiceItems")
                .content(invoiceItemJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedInvoiceItemJson));


        mockMvc.perform(MockMvcRequestBuilders.post("/invoiceItems")
                .content(invoiceItemMissingAPropertyJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void testGetInvoiceItem() throws Exception {

        Mockito.when(itemDao.getInvoiceItemById(0)).thenReturn(null);
        Mockito.when(itemDao.getInvoiceItemById(1)).thenReturn(expectedInvoiceItem);

        String expectedInvoiceItemJson = om.writeValueAsString(expectedInvoiceItem);

        mockMvc.perform(MockMvcRequestBuilders.get("/invoiceItems/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedInvoiceItemJson));

        mockMvc.perform(MockMvcRequestBuilders.get("/invoiceItems/0"))
                .andDo(print())
                .andExpect(content().string(""));
    }

    @Test
    public void testGetAllInvoiceItems() throws Exception {
        InvoiceItem invoiceItem1 = new InvoiceItem(
                9,
                5,
                1,
                new BigDecimal(6.00).setScale(2, RoundingMode.FLOOR));
        invoiceItem1.setInvoiceItemId(2);

        InvoiceItem invoiceItem2 = new InvoiceItem(
                9,
                8,
                2,
                new BigDecimal(12.00).setScale(2, RoundingMode.FLOOR));
        invoiceItem2.setInvoiceItemId(3);

        List<InvoiceItem> allInvoiceItems = new ArrayList<>();
        allInvoiceItems.add(invoiceItem1);
        allInvoiceItems.add(invoiceItem2);

        String allInvoiceItemsJson = om.writeValueAsString(allInvoiceItems);

        Mockito.when(itemDao.getAllInvoiceItems()).thenReturn(allInvoiceItems);

        mockMvc.perform(MockMvcRequestBuilders.get("/invoiceItems"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allInvoiceItemsJson));
    }

    @Test
    public void deleteInvoiceItem() throws Exception {

        String successful = "Deletion successful. InvoiceItem with invoiceItem '" + 1 + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No invoiceItem with invoiceItem '" + 0 + "' was found.";

        Mockito.when(itemDao.deleteInvoiceItem(0)).thenReturn(unsuccessful);
        Mockito.when(itemDao.deleteInvoiceItem(1)).thenReturn(successful);

        mockMvc.perform(MockMvcRequestBuilders.delete("/invoiceItems/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successful));

        mockMvc.perform(MockMvcRequestBuilders.delete("/invoiceItems/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(unsuccessful));

    }

    @Test
    public void updateInvoiceItem() throws Exception {
        InvoiceItem invoiceItem0NotAllowed = new InvoiceItem(
                9,
                8,
                2,
                new BigDecimal(12.00).setScale(2, RoundingMode.FLOOR));
        invoiceItem0NotAllowed.setInvoiceItemId(0);

        InvoiceItem invoiceItem4NotFound = new InvoiceItem(
                9,
                6,
                1,
                new BigDecimal(6.00).setScale(2, RoundingMode.FLOOR));
        invoiceItem4NotFound.setInvoiceItemId(4);

        String invoiceItem0NotAllowedJson = om.writeValueAsString(invoiceItem0NotAllowed);
        String inventory4NotFoundJson = om.writeValueAsString(invoiceItem4NotFound);
        String invoiceItemJson = om.writeValueAsString(expectedInvoiceItem);

        String successful = "Update successful: "+ expectedInvoiceItem.toString();
        String unsuccessful = "Update NOT successful. Please try again.";

        Mockito.when(itemDao.updateInvoiceItem(invoiceItem4NotFound)).thenReturn(unsuccessful);
        Mockito.when(itemDao.updateInvoiceItem(expectedInvoiceItem)).thenReturn(successful);

        mockMvc.perform(MockMvcRequestBuilders.put("/invoiceItems")
                .content(invoiceItemJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successful));

        mockMvc.perform(MockMvcRequestBuilders.put("/invoiceItems")
                .content(inventory4NotFoundJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(unsuccessful));

        mockMvc.perform(MockMvcRequestBuilders.put("/invoiceItems")
                .content(invoiceItem0NotAllowedJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnprocessableEntity());
    }
}
