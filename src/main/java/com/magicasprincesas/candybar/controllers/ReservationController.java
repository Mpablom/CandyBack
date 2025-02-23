package com.magicasprincesas.candybar.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.magicasprincesas.candybar.exceptions.CustomException;
import com.magicasprincesas.candybar.services.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

  @Autowired
  private ReservationService reservationService;

  @PostMapping
  public ResponseEntity<?> createReservation(@RequestBody ReservationRequestDto request) {
    try {
      ReservationResponseDto response = reservationService.saveReservation(request);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (CustomException e) {
      return ResponseEntity.badRequest().body(
          Map.of(
              "timestamp", LocalDateTime.now(),
              "message", e.getMessage()));
    }
  }

  @GetMapping("/date/{date}")
  public List<ReservationResponseDto> getReservationsByDate(@PathVariable String date) {
    LocalDate reservationDate = LocalDate.parse(date);
    return reservationService.getReservationsByDate(reservationDate);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable Long id) {
    try {
      ReservationResponseDto response = reservationService.getReservationById(id);
      return ResponseEntity.ok(response);
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @GetMapping
  public List<ReservationResponseDto> getAllReservations() {
    return reservationService.getAllReservations();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> deleteReservation(@PathVariable Long id) {
    reservationService.deleteReservation(id);
    Map<String, String> response = new HashMap<>();
    response.put("message", "Reservation deleted successfully");
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ReservationResponseDto> updateReservation(
      @PathVariable Long id,
      @RequestBody ReservationRequestDto request) {

    ReservationResponseDto response = reservationService.updateReservation(id, request);
    return ResponseEntity.ok(response);
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

  @DeleteMapping("/past/all")
  public ResponseEntity<Map<String, String>> deletePastReservationsAndCustomers() {
    reservationService.deletePastReservationsAndCustomers();

    Map<String, String> response = new HashMap<>();
    response.put("message", "Reservas pasadas y clientes asociados eliminados correctamente");

    return ResponseEntity.ok(response);
  }
}
