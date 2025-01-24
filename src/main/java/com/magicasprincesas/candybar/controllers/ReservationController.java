package com.magicasprincesas.candybar.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magicasprincesas.candybar.dtos.ReservationRequestDto;
import com.magicasprincesas.candybar.dtos.ReservationResponseDto;
import com.magicasprincesas.candybar.services.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

  @Autowired
  private ReservationService reservationService;

  @PostMapping
  public ReservationResponseDto createReservation(@RequestBody ReservationRequestDto request) {
    return reservationService.saveReservation(request);
  }

  @GetMapping("/date/{date}")
  public List<ReservationResponseDto> getReservationsByDate(@PathVariable String date) {
    LocalDate reservationDate = LocalDate.parse(date);
    return reservationService.getReservationsByDate(reservationDate);
  }

  @GetMapping
  public List<ReservationResponseDto> getAllReservations() {
    return reservationService.getAllReservations();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
    reservationService.deleteReservation(id);
    return ResponseEntity.ok("Reservation deleted successfully");
  }

  @PutMapping("/{id}")
  public ReservationResponseDto updateReservation(@PathVariable Long id, @RequestBody ReservationRequestDto request) {
    return reservationService.updateReservation(id, request);
  }

  @DeleteMapping("/past")
  public ResponseEntity<String> deletePastReservations() {
    reservationService.deletePastReservations();
    return ResponseEntity.ok("Past reservations deleted successfully");
  }

  @DeleteMapping("/past/customers")
  public ResponseEntity<String> deleteCustomersWithPastReservations() {
    reservationService.deleteCustomersWithPastReservations();
    return ResponseEntity.ok("Customers with past reservations deleted successfully");
  }

  @PutMapping("/recover/{id}")
  public ReservationResponseDto recoverReservation(@PathVariable Long id) {
    return reservationService.recoverReservation(id);
  }
}
