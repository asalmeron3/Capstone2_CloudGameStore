package com.capstone2.gamestoreinvoiceservice.controller;

import com.capstone2.gamestoreinvoiceservice.dao.InvoiceDaoJdbcTemplateImpl;
import com.capstone2.gamestoreinvoiceservice.dao.InvoiceItemDaoJdbcTemplateImpl;
import com.capstone2.gamestoreinvoiceservice.model.Invoice;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@RunWith(SpringRunner.class)
@WebMvcTest
public class InvoiceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceDaoJdbcTemplateImpl invoiceDao;

    @MockBean
    private InvoiceItemDaoJdbcTemplateImpl invoiceItem;

    private ObjectMapper om = new ObjectMapper();
    private Invoice invoiceExpected;

    @Before
    public void setUp() {
        invoiceExpected = new Invoice(9, LocalDate.of(2019, 07, 27));
        invoiceExpected.setInvoiceId(1);
    }

    @Test
    public void testPostInvoice() throws Exception {

        Invoice invoiceInfo = new Invoice(9, LocalDate.of(2019, 07, 27));

        Invoice invoiceNoCustomerId = new Invoice();
        invoiceNoCustomerId.setPurchaseDate(LocalDate.of(2019, 07, 27));

        Invoice invoiceNoPurchaseDate = new Invoice();
        invoiceNoPurchaseDate.setCustomerId(9);

        Mockito.when(invoiceDao.createInvoice(invoiceInfo)).thenReturn(invoiceExpected);

        String expectedInvoiceJson = om.writeValueAsString(invoiceExpected);
        String invoiceInfoJson = om.writeValueAsString(invoiceInfo);
        String invoiceNoCustomerIdJson = om.writeValueAsString(invoiceNoCustomerId);
        String invoiceNoPurchaseDateJson = om.writeValueAsString(invoiceNoPurchaseDate);


        mockMvc.perform(MockMvcRequestBuilders.post("/invoices")
                .content(invoiceInfoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedInvoiceJson));

        mockMvc.perform(MockMvcRequestBuilders.post("/invoices")
                .content(invoiceNoCustomerIdJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(MockMvcRequestBuilders.post("/invoices")
                .content(invoiceNoPurchaseDateJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void testGetInvoice() throws Exception {

        Mockito.when(invoiceDao.getInvoiceById(0)).thenReturn(null);
        Mockito.when(invoiceDao.getInvoiceById(1)).thenReturn(invoiceExpected);

        String expectedInvoiceJson = om.writeValueAsString(invoiceExpected);

        mockMvc.perform(MockMvcRequestBuilders.get("/invoices/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedInvoiceJson));

        mockMvc.perform(MockMvcRequestBuilders.get("/invoices/0"))
                .andDo(print())
                .andExpect(content().string(""));
    }

    @Test
    public void testGetAllInvoices() throws Exception {
        Invoice invoiceInfo1 = new Invoice(9, LocalDate.of(2019, 07, 27));
        invoiceInfo1.setInvoiceId(2);

        Invoice invoiceInfo2 = new Invoice(8, LocalDate.of(2019, 07, 27));
        invoiceInfo2.setInvoiceId(3);

        List<Invoice> allInvoices = new ArrayList<>();
        allInvoices.add(invoiceInfo1);
        allInvoices.add(invoiceInfo2);

        String allInvoicesJson = om.writeValueAsString(allInvoices);

        Mockito.when(invoiceDao.getAllInvoices()).thenReturn(allInvoices);

        mockMvc.perform(MockMvcRequestBuilders.get("/invoices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allInvoicesJson));
    }

    @Test
    public void deleteInvoice() throws Exception {

        String successful = "Deletion successful. Invoice with inventoryId '" + 1 + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No inventory with inventoryId '" + 0 + "' was found.";

        Mockito.when(invoiceDao.deleteInvoice(0)).thenReturn(unsuccessful);
        Mockito.when(invoiceDao.deleteInvoice(1)).thenReturn(successful);

        mockMvc.perform(MockMvcRequestBuilders.delete("/invoices/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successful));

        mockMvc.perform(MockMvcRequestBuilders.delete("/invoices/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(unsuccessful));

    }

    @Test
    public void updateInvoice() throws Exception {
        Invoice inventoryId0NotAllowed = new Invoice(5, LocalDate.of(2019, 07, 27));
        inventoryId0NotAllowed.setInvoiceId(0);

        Invoice inventoryId4NotFound = new Invoice(10, LocalDate.of(2019, 07, 27));
        inventoryId4NotFound.setInvoiceId(4);

        String inventoryId0NotAllowedJson = om.writeValueAsString(inventoryId0NotAllowed);
        String inventory4NotFoundJson = om.writeValueAsString(inventoryId4NotFound);
        String invoiceExpectedJson = om.writeValueAsString(invoiceExpected);

        String successful = "Update successful: "+ invoiceExpected.toString();
        String unsuccessful = "Update NOT successful. Please try again.";

        Mockito.when(invoiceDao.updateInvoice(inventoryId4NotFound)).thenReturn(unsuccessful);
        Mockito.when(invoiceDao.updateInvoice(invoiceExpected)).thenReturn(successful);

        mockMvc.perform(MockMvcRequestBuilders.put("/invoices")
                .content(invoiceExpectedJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successful));

        mockMvc.perform(MockMvcRequestBuilders.put("/invoices")
                .content(inventory4NotFoundJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(unsuccessful));

        mockMvc.perform(MockMvcRequestBuilders.put("/invoices")
                .content(inventoryId0NotAllowedJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testGetAllInvoicesByCustomerId() throws Exception {
        Invoice invoiceInfo1 = new Invoice(9, LocalDate.of(2019, 07, 27));
        invoiceInfo1.setInvoiceId(2);

        Invoice invoiceInfo2 = new Invoice(8, LocalDate.of(2019, 07, 27));
        invoiceInfo2.setInvoiceId(3);

        List<Invoice> invoicesCustomer8 = Arrays.asList(invoiceInfo2);
        List<Invoice> invoicesCustomer9 = Arrays.asList(invoiceInfo1);

        String invoicesCustomer8Json = om.writeValueAsString(invoicesCustomer8);
        String invoicesCustomer9Json = om.writeValueAsString(invoicesCustomer9);

        Mockito.when(invoiceDao.getAllInvoicesForCustomerId(8)).thenReturn(invoicesCustomer8);
        Mockito.when(invoiceDao.getAllInvoicesForCustomerId(9)).thenReturn(invoicesCustomer9);


        mockMvc.perform(MockMvcRequestBuilders.get("/invoices/customer/8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(invoicesCustomer8Json));

        mockMvc.perform(MockMvcRequestBuilders.get("/invoices/customer/9"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(invoicesCustomer9Json));
    }
}
