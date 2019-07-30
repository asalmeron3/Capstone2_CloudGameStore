package com.capstone2.gamestoreretailservice.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Customer {

    private int customerId;

    @NotNull(message = "'firstName' must be included in this customer")
    @Size(max = 50, message = "'firstName' cannot exceed 50 characters")
    private String firstName;

    @NotNull(message = "'lastName' must be included in this customer")
    @Size(max = 50, message = "'lastName' cannot exceed 50 characters")
    private String lastName;

    @NotNull(message = "'street' must be included in this customer")
    @Size(max = 50, message = "'street' cannot exceed 50 characters")
    private String street;

    @NotNull(message = "'city' must be included in this customer")
    @Size(max = 50, message = "'city' cannot exceed 50 characters")
    private String city;

    @NotNull(message = "'zip' must be included in this customer")
    @Size(max = 10, message = "'zip' cannot exceed 10 characters")
    private String zip;

    @NotNull(message = "'email' must be included in this customer")
    @Size(max = 75, message = "'email' cannot exceed 75 characters")
    private String email;

    @NotNull(message = "'phone' must be included in this customer")
    @Size(max = 20, message = "'phone' cannot exceed 20 characters")
    private String phone;

    public Customer() {}

    public Customer(String firstName, String lastName, String street, String city, String zip, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.email = email;
        this.phone = phone;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId == customer.customerId &&
                Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(street, customer.street) &&
                Objects.equals(city, customer.city) &&
                Objects.equals(zip, customer.zip) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(phone, customer.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, firstName, lastName, street, city, zip, email, phone);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
