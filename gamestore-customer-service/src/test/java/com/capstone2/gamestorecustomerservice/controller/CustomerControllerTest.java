package com.capstone2.gamestorecustomerservice.controller;

import com.capstone2.gamestorecustomerservice.dao.CustomerDaoTemplateImpl;
import com.capstone2.gamestorecustomerservice.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@RunWith(SpringRunner.class)
@WebMvcTest
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerDaoTemplateImpl customerDao;

    private ObjectMapper om = new ObjectMapper();
    private Customer customerExpected;

    @Before
    public void setUp() {
        customerExpected = new Customer(
                "Anthony",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );

        customerExpected.setCustomerId(1);
    }

    @Test
    public void testPostCustomer() throws Exception {

        Customer customerInfo = new Customer(
                "Anthony",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );

        Customer blankCustomer = new Customer();
        blankCustomer.setLastName("Williams");

        Mockito.when(customerDao.createCustomer(customerInfo)).thenReturn(customerExpected);

        String expectedCustomerJson = om.writeValueAsString(customerExpected);
        String customerInfoJson = om.writeValueAsString(customerInfo);
        String blankCustomerJson = om.writeValueAsString(blankCustomer);

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                .content(customerInfoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedCustomerJson));

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                .content(blankCustomerJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void testGetCustomer() throws Exception {

        Mockito.when(customerDao.getCustomerById(0)).thenReturn(null);
        Mockito.when(customerDao.getCustomerById(1)).thenReturn(customerExpected);

        String expectedCustomerJson = om.writeValueAsString(customerExpected);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedCustomerJson));

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/0"))
                .andDo(print())
                .andExpect(content().string(""));
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        Customer customerInfo1 = new Customer(
                "Tony",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );
        customerInfo1.setCustomerId(2);

        Customer customerInfo2 = new Customer(
                "Anthony",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );
        customerInfo2.setCustomerId(3);

        List<Customer> customers = new ArrayList<>();
        customers.add(customerInfo1);
        customers.add(customerInfo2);

        String customersJson = om.writeValueAsString(customers);

        Mockito.when(customerDao.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(customersJson));
    }

    @Test
    public void deleteCustomer() throws Exception {

        String successful = "Deletion successful. Customer with customer_id '" + 1 + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No customer with customer_id '" + 0 + "' was found.";

        Mockito.when(customerDao.deleteCustomer(0)).thenReturn(unsuccessful);
        Mockito.when(customerDao.deleteCustomer(1)).thenReturn(successful);

        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successful));

        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(unsuccessful));

    }

    @Test
    public void updateCustomer() throws Exception {
        Customer customerInfo0 = new Customer(
                "Tony",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );
        customerInfo0.setCustomerId(0);

        Customer customerInfo4 = new Customer(
                "Mertyle",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );
        customerInfo4.setCustomerId(4);

        String customer0Json = om.writeValueAsString(customerInfo0);
        String customer4Json = om.writeValueAsString(customerInfo4);
        String customerExpectedJson = om.writeValueAsString(customerExpected);

        String successful = "Update successful: "+ customerExpected.toString();
        String unsuccessful = "Update NOT successful. Please try again.";

        Mockito.when(customerDao.updateCustomer(customerInfo4)).thenReturn(unsuccessful);
        Mockito.when(customerDao.updateCustomer(customerExpected)).thenReturn(successful);

        mockMvc.perform(MockMvcRequestBuilders.put("/customers")
                .content(customerExpectedJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successful));

        mockMvc.perform(MockMvcRequestBuilders.put("/customers")
                .content(customer4Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(unsuccessful));

        mockMvc.perform(MockMvcRequestBuilders.put("/customers")
                .content(customer0Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnprocessableEntity());
    }
}
