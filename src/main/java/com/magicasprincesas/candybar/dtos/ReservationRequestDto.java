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
public class ReservationRequestDto {
  private LocalDate reservationDate;
  private double deposit;
  private boolean deleted;
  private Long customerId;
}
