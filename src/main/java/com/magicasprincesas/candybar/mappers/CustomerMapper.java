package com.magicasprincesas.candybar.mappers;

import org.springframework.stereotype.Component;

import com.magicasprincesas.candybar.dtos.CustomerRequestDto;
import com.magicasprincesas.candybar.dtos.CustomerResponseDto;
import com.magicasprincesas.candybar.entities.Customer;

@Component
public class CustomerMapper {

  public Customer toEntity(CustomerRequestDto requestDto) {
    return Customer.builder()
        .firstName(requestDto.getFirstName())
        .lasName(requestDto.getLastName())
        .phone(requestDto.getPhone())
        .build();
  }

  public CustomerResponseDto toDto(Customer customer) {
    return CustomerResponseDto.builder()
        .id(customer.getId())
        .firstName(customer.getFirstName())
        .lastName(customer.getLasName())
        .phone(customer.getPhone())
        .build();
  }
}
