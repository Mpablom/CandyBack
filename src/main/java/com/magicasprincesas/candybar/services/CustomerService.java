package com.magicasprincesas.candybar.services;

import java.util.List;

import com.magicasprincesas.candybar.dtos.CustomerRequestDto;
import com.magicasprincesas.candybar.dtos.CustomerResponseDto;

public interface CustomerService {
  CustomerResponseDto saveCustomer(CustomerRequestDto requestDto);
  List<CustomerResponseDto> getAllCustomers();
  CustomerResponseDto getCustomerById(Long id);
  CustomerResponseDto updateCustomer(Long id, CustomerRequestDto request);
}
