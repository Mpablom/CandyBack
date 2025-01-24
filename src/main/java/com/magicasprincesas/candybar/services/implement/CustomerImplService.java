package com.magicasprincesas.candybar.services.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magicasprincesas.candybar.dtos.CustomerRequestDto;
import com.magicasprincesas.candybar.dtos.CustomerResponseDto;
import com.magicasprincesas.candybar.entities.Customer;
import com.magicasprincesas.candybar.exceptions.CustomException;
import com.magicasprincesas.candybar.mappers.CustomerMapper;
import com.magicasprincesas.candybar.repositories.CustomerRepository;
import com.magicasprincesas.candybar.services.CustomerService;

@Service
public class CustomerImplService implements CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerMapper customerMapper;

  @Override
  public CustomerResponseDto saveCustomer(CustomerRequestDto requestDto) {
    Customer customer = customerMapper.toEntity(requestDto);
    customer = customerRepository.save(customer);
    return customerMapper.toDto(customer);
  }

  @Override
  public List<CustomerResponseDto> getAllCustomers() {
    return customerRepository.findAll().stream()
        .map(customerMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public CustomerResponseDto getCustomerById(Long id) {
    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new CustomException("Customer not found with id: " + id));
    return customerMapper.toDto(customer);
  }

  @Override
  public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto request) {
    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new CustomException("Customer not found with id: " + id));

    customer.setFirstName(request.getFirstName());
    customer.setLasName(request.getLastName());
    customer.setPhone(request.getPhone());

    customer = customerRepository.save(customer);
    return customerMapper.toDto(customer);
  }
}
