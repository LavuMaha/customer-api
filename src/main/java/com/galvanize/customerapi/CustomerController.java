package com.galvanize.customerapi;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/api/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @GetMapping("/api/customers")
    @ResponseStatus(HttpStatus.OK)
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/api/customers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Customer getCustomer(@PathVariable UUID id) throws HttpClientErrorException {
        Customer customer = customerService.getCustomer(id);
        if (customer == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        } else {
            return customer;
        }
    }

}
