package com.magicasprincesas.candybar.services.implement;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magicasprincesas.candybar.dtos.ReservationRequestDto;
import com.magicasprincesas.candybar.dtos.ReservationResponseDto;
import com.magicasprincesas.candybar.entities.Customer;
import com.magicasprincesas.candybar.entities.Reservation;
import com.magicasprincesas.candybar.exceptions.CustomException;
import com.magicasprincesas.candybar.mappers.ReservationMapper;
import com.magicasprincesas.candybar.repositories.CustomerRepository;
import com.magicasprincesas.candybar.repositories.ReservationRepository;
import com.magicasprincesas.candybar.services.ReservationService;

import jakarta.transaction.Transactional;

@Service
public class ReservationImplService implements ReservationService {

  @Autowired
  private ReservationRepository reservationRepository;

  @Autowired
  private ReservationMapper reservationMapper;

  @Autowired
  private CustomerRepository customerRepository;

  @Override
  public ReservationResponseDto saveReservation(ReservationRequestDto requestDto) {
    LocalDate reservationDate = requestDto.getReservationDate();
    LocalDate currentDate = LocalDate.now();

    if (reservationDate.isBefore(currentDate)) {
      throw new CustomException("Reservation date cannot be in the past.");
    }

    int reservationsCount = reservationRepository.countByReservationDate(reservationDate);
    if (reservationsCount >= 3) {
      throw new CustomException("Cannot book more than 3 reservations per day.");
    }
    Customer customer = customerRepository.findById(requestDto.getCustomerId())
        .orElseThrow(() -> new CustomException("Cliente no encontrado con ID: " + requestDto.getCustomerId()));

    Reservation reservation = reservationMapper.toEntity(requestDto);
    reservation.setCustomer(customer);
    reservation = reservationRepository.save(reservation);
    return reservationMapper.toDto(reservation);
  }

  @Override
  public List<ReservationResponseDto> getReservationsByDate(LocalDate date) {
    return reservationRepository.findByReservationDate(date).stream()
        .map(reservationMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<ReservationResponseDto> getAllReservations() {
    return reservationRepository.findAll().stream()
        .map(reservationMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteReservation(Long id) {
    Reservation reservation = reservationRepository.findById(id)
        .orElseThrow(() -> new CustomException("Reservation not found with id: " + id));
    reservation.setDeleted(true);
    reservationRepository.save(reservation);
  }

  @Override
  public ReservationResponseDto updateReservation(Long id, ReservationRequestDto request) {
    Reservation reservation = reservationRepository.findById(id)
        .orElseThrow(() -> new CustomException("Reservation not found with id: " + id));

    Customer customer = customerRepository.findById(request.getCustomerId())
        .orElseThrow(() -> new CustomException("Customer not found with id: " + request.getCustomerId()));

    reservation.setReservationDate(request.getReservationDate());
    reservation.setDeposit(request.getDeposit());
    reservation.setCustomer(customer);

    reservation = reservationRepository.save(reservation);
    return reservationMapper.toDto(reservation);
  }

  @Override
  @Transactional
  public void deletePastReservations() {
    LocalDate currentDate = LocalDate.now();
    List<Reservation> pastReservations = reservationRepository.findByReservationDateBefore(currentDate);

    reservationRepository.deleteAll(pastReservations);
  }

  @Override
  @Transactional
  public void deleteCustomersWithPastReservations() {
    LocalDate currentDate = LocalDate.now();
    List<Reservation> pastReservations = reservationRepository.findByReservationDateBefore(currentDate);

    List<Customer> customersToDelete = pastReservations.stream()
        .map(Reservation::getCustomer)
        .distinct()
        .collect(Collectors.toList());

    customerRepository.deleteAll(customersToDelete);
  }

  @Override
  @Transactional
  public void deletePastReservationsAndCustomers() {
    LocalDate currentDate = LocalDate.now();

    List<Reservation> pastReservations = reservationRepository.findByReservationDateBefore(currentDate);
    reservationRepository.deleteAll(pastReservations);

    List<Customer> customersWithPastReservations = pastReservations.stream()
        .map(Reservation::getCustomer)
        .distinct()
        .collect(Collectors.toList());

    for (Customer customer : customersWithPastReservations) {
      List<Reservation> customerReservations = reservationRepository.findByCustomer(customer);

      boolean hasFutureReservations = customerReservations.stream()
          .anyMatch(reservation -> reservation.getReservationDate().isAfter(currentDate) ||
              reservation.getReservationDate().isEqual(currentDate));

      if (!hasFutureReservations) {
        customerRepository.delete(customer);
      }
    }
  }

  @Override
  @Transactional
  public ReservationResponseDto recoverReservation(Long id) {
    Reservation reservation = reservationRepository.findByIdAndDeletedTrue(id)
        .orElseThrow(() -> new CustomException("Deleted reservation not found with id: " + id));

    reservation.setDeleted(false);
    reservation = reservationRepository.save(reservation);
    return reservationMapper.toDto(reservation);
  }
}
