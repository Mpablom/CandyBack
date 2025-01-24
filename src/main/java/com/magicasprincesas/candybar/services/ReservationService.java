package com.magicasprincesas.candybar.services;

import java.time.LocalDate;
import java.util.List;

import com.magicasprincesas.candybar.dtos.ReservationRequestDto;
import com.magicasprincesas.candybar.dtos.ReservationResponseDto;

public interface ReservationService {
  ReservationResponseDto saveReservation(ReservationRequestDto requestDto);
  List<ReservationResponseDto> getReservationsByDate(LocalDate date);
  List<ReservationResponseDto> getAllReservations();
  void deleteReservation(Long id);
  ReservationResponseDto updateReservation(Long id, ReservationRequestDto request);
  void deletePastReservations();
  void deleteCustomersWithPastReservations();
  ReservationResponseDto recoverReservation(Long id);
}
