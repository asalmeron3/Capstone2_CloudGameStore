package com.capstone2.gamestorecustomerservice.dao;

import com.capstone2.gamestorecustomerservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerDaoTemplateImpl implements CustomerDao {

    @Autowired
    JdbcTemplate customerJdbc;

    public CustomerDaoTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.customerJdbc = jdbcTemplate;
    }

    public Customer mapRowToCustomer(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer();

        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setStreet(rs.getString("street"));
        customer.setCity(rs.getString("city"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setZip(rs.getString("zip"));

        return customer;
    }

    private static final String GET_ALL_CUSTOMERS =
            "select * from customer";
    private static final String CREATE_A_CUSTOMER =
            "insert into customer (first_name, last_name, street, city, zip, email, phone) values (?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_CUSTOMER_BY_ID =
            "select * from customer where customer_id = ?";
    private static final String UPDATE_CUSTOMER =
            "update customer set first_name = ?, last_name = ?, street = ?, city = ?, zip = ?, email = ?, phone = ?" +
                    "where customer_id = ?";
    private static final String DELETE_CUSTOMER =
            "delete from customer where customer_id = ?";

    @Override
    public List<Customer> getAllCustomers() {
        return customerJdbc.query(GET_ALL_CUSTOMERS, this::mapRowToCustomer);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        customerJdbc.update(
                CREATE_A_CUSTOMER,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getStreet(),
                customer.getCity(),
                customer.getZip(),
                customer.getEmail(),
                customer.getPhone()
        );

        int id = customerJdbc.queryForObject("select LAST_INSERT_ID()", Integer.class);

        customer.setCustomerId(id);

        return customer;
    }

    @Override
    public Customer getCustomerById(int id) {
        try {
            return customerJdbc.queryForObject(GET_CUSTOMER_BY_ID, this::mapRowToCustomer, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String updateCustomer(Customer customer) {
        int rowsUpdated = customerJdbc.update(
                UPDATE_CUSTOMER,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getStreet(),
                customer.getCity(),
                customer.getZip(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCustomerId()
        );

        String successful = "Update successful: "+ customer.toString();
        String unsuccessful = "Update NOT successful. Please try again.";

        return rowsUpdated == 1 ? successful : unsuccessful;
    }

    @Override
    public String deleteCustomer(int id) {
        int rowsUpdated = customerJdbc.update(DELETE_CUSTOMER,id);

        String successful = "Deletion successful. Customer with customer_id '" + id + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No customer with customer_id '" + id + "' was found.";

        return rowsUpdated == 1 ? successful : unsuccessful;
    }
}
