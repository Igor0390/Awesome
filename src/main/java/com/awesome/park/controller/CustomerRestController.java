package com.awesome.park.controller;

import com.awesome.park.api.CustomerApi;
import com.awesome.park.dto.CustomerDto;
import com.awesome.park.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerRestController implements CustomerApi {
    private final CustomerService customerService;

    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    public ResponseEntity<CustomerDto> getCustomerById(Long id) {
        CustomerDto customer = customerService.getCustomerById(id);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<CustomerDto> createCustomer(CustomerDto customerDto) {
        CustomerDto createdCustomer = customerService.createOrUpdateCustomer(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    public ResponseEntity<CustomerDto> updateCustomer(Long id, CustomerDto customerDto) {
        CustomerDto updatedCustomer = customerService.createOrUpdateCustomer(customerDto);
        return ResponseEntity.ok(updatedCustomer);
    }

    public ResponseEntity<Void> deleteCustomer(Long id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }
}
