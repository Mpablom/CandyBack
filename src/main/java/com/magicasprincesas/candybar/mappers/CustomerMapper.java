package com.magicasprincesas.candybar.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.magicasprincesas.candybar.dtos.CustomerRequestDto;
import com.magicasprincesas.candybar.dtos.CustomerResponseDto;
import com.magicasprincesas.candybar.entities.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
  Customer toEntity(CustomerRequestDto requestDto);

  @Mapping(target = "lastName", source = "lastName")
  CustomerResponseDto toDto(Customer customer);
}
