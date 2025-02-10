package com.magicasprincesas.candybar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magicasprincesas.candybar.dtos.CustomerRequestDto;
import com.magicasprincesas.candybar.dtos.CustomerResponseDto;
import com.magicasprincesas.candybar.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @PostMapping("/customers")
  public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerRequestDto requestDto) {
    CustomerResponseDto customer = customerService.saveCustomer(requestDto);
    return new ResponseEntity<>(customer, HttpStatus.CREATED);
  }

  @GetMapping
  public List<CustomerResponseDto> getAllCustomers() {
    return customerService.getAllCustomers();
  }

  @GetMapping("/{id}")
  public CustomerResponseDto getCustomerById(@PathVariable Long id) {
    return customerService.getCustomerById(id);
  }

  @PutMapping("/{id}")
  public CustomerResponseDto updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDto request) {
    return customerService.updateCustomer(id, request);
  }
}
