package com.galvanize.customerapi;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    private List<Customer> customers = new ArrayList<>();
    public Customer addCustomer(Customer customer) {

        UUID id = UUID.randomUUID();
        customer.setId(id);
        return customer;
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        Customer cust1=new Customer(UUID.randomUUID(),"Salvator","Di'Mario","45 Carver Ave, Midland, TX 70134", "510-555-7863");
        Customer cust2=new Customer(UUID.randomUUID(),"Qin","Zhang","1 Main Street, Topeka, KS 37891", "510-555-2367");
        customers.add(cust1);
        customers.add(cust2);
        return customers;
    }

    public Customer getCustomer(UUID id) {
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }

}
