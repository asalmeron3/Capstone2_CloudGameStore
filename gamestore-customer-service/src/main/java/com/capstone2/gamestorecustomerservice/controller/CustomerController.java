package com.capstone2.gamestorecustomerservice.controller;

import com.capstone2.gamestorecustomerservice.dao.CustomerDaoTemplateImpl;
import com.capstone2.gamestorecustomerservice.exception.NotFoundException;
import com.capstone2.gamestorecustomerservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerDaoTemplateImpl customerDao;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer addCustomer(@RequestBody @Valid Customer customer) {return customerDao.createCustomer(customer);}

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Customer> getAllCustomers() {return customerDao.getAllCustomers();}

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Customer getCustomerById(@PathVariable int id) {
        return customerDao.getCustomerById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String updateCustomer(@RequestBody @Valid Customer customer) {
        if(customer.getCustomerId() == 0) {
            throw new NotFoundException("An 'customerId' is required to update a customer");
        }
        return customerDao.updateCustomer(customer);}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteCustomer(@PathVariable int id) {return customerDao.deleteCustomer(id);}


}
