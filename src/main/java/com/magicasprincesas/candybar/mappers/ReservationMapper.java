package com.magicasprincesas.candybar.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.magicasprincesas.candybar.dtos.ReservationRequestDto;
import com.magicasprincesas.candybar.dtos.ReservationResponseDto;
import com.magicasprincesas.candybar.entities.Customer;
import com.magicasprincesas.candybar.entities.Reservation;
import com.magicasprincesas.candybar.exceptions.CustomException;
import com.magicasprincesas.candybar.repositories.CustomerRepository;

@Component
public class ReservationMapper {

  @Autowired
  private CustomerMapper customerMapper;

  @Autowired
  private CustomerRepository customerRepository;

  public Reservation toEntity(ReservationRequestDto requestDto) {
    Customer customer = customerRepository.findById(requestDto.getCustomerId())
        .orElseThrow(() -> new CustomException("Customer not found with id: " + requestDto.getCustomerId()));
    return Reservation.builder()
        .reservationDate(requestDto.getReservationDate())
        .deposit(requestDto.getDeposit())
        .customer(customer)
        .build();
  }

  public ReservationResponseDto toDto(Reservation reservation) {
    return ReservationResponseDto.builder()
        .id(reservation.getId())
        .reservationDate(reservation.getReservationDate())
        .deposit(reservation.getDeposit())
        .deleted(reservation.isDeleted())
        .customer(customerMapper.toDto(reservation.getCustomer()))
        .build();
  }
}
