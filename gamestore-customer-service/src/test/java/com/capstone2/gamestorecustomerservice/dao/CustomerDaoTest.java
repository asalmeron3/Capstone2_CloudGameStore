package com.capstone2.gamestorecustomerservice.dao;

import com.capstone2.gamestorecustomerservice.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;

    private Customer expectedCustomer;

    @Before
    public void setUp() {
        List<Customer> allCustomers = customerDao.getAllCustomers();

        allCustomers.stream().forEach(customer -> customerDao.deleteCustomer(customer.getCustomerId()));

        expectedCustomer = new Customer(
                "Anthony",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );
    }

    @Test
    public void createGetDeleteCustomer() {
        Customer customerToCreate = new Customer(
                "Anthony",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );

        Customer actualCustomer = customerDao.createCustomer(customerToCreate);

        int id = actualCustomer.getCustomerId();
        expectedCustomer.setCustomerId(id);

        Assert.assertEquals(expectedCustomer, actualCustomer);

        Customer customerFound = customerDao.getCustomerById(id);
        Assert.assertEquals(expectedCustomer, customerFound);

        String successful = "Deletion successful. Customer with customer_id '" + id + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No customer with customer_id '" + 0 + "' was found.";

        String deletedMessage = customerDao.deleteCustomer(id);
        String notDeletedMessage = customerDao.deleteCustomer(0);

        Assert.assertEquals(successful, deletedMessage);
        Assert.assertEquals(unsuccessful, notDeletedMessage);
    }

    @Test
    public void updateCustomer() {
        Customer customerToCreate = new Customer(
                "Anthony",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );

        Customer customerNotFound = new Customer(
                "Anthony",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );
        customerNotFound.setCustomerId(0);

        Customer actualCustomer = customerDao.createCustomer(customerToCreate);

        int id = actualCustomer.getCustomerId();

        expectedCustomer.setCustomerId(id);
        expectedCustomer.setLastName("Daniels");
        expectedCustomer.setPhone("999-999-9999");

        String successful = "Update successful: "+ expectedCustomer.toString();
        String unsuccessful = "Update NOT successful. Please try again.";

        String updatedMessage = customerDao.updateCustomer(expectedCustomer);
        String notUpdatedMessage = customerDao.updateCustomer(customerNotFound);

        Assert.assertEquals(successful, updatedMessage);
        Assert.assertEquals(unsuccessful, notUpdatedMessage);
    }

    @Test
    public void getAllCustomers() {
        Customer customer1 = new Customer(
                "Anthony",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );
        Customer customer2 = new Customer(
                "Tony",
                "Smith",
                "1111 Somewhere Drive",
                "Chicago",
                "60601",
                "anthonysmith@email.com",
                "888-888-8888"
        );

        customer1 = customerDao.createCustomer(customer1);
        customer2 = customerDao.createCustomer(customer2);

        List<Customer> customers = customerDao.getAllCustomers();

        Assert.assertEquals(2, customers.size());

    }
}
