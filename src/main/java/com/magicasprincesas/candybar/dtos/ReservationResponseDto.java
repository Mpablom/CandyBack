package com.magicasprincesas.candybar.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
  private Long id;
  private LocalDate reservationDate;
  private double deposit;
  private boolean deleted;
  private String location;
  private String description;
  private String starTime;
  private CustomerResponseDto customer;
}
