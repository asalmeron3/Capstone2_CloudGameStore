package com.capstone2.gamestorecustomerservice.dao;

import com.capstone2.gamestorecustomerservice.model.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> getAllCustomers();

    Customer createCustomer(Customer customer);
    Customer getCustomerById(int id);

    String updateCustomer(Customer customer);
    String deleteCustomer(int id);
}
