package com.magicasprincesas.candybar.services.implement;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.magicasprincesas.candybar.dtos.CustomerRequestDto;
import com.magicasprincesas.candybar.dtos.ReservationRequestDto;
import com.magicasprincesas.candybar.dtos.ReservationResponseDto;
import com.magicasprincesas.candybar.entities.Customer;
import com.magicasprincesas.candybar.entities.Reservation;
import com.magicasprincesas.candybar.exceptions.CustomException;
import com.magicasprincesas.candybar.mappers.CustomerMapper;
import com.magicasprincesas.candybar.mappers.ReservationMapper;
import com.magicasprincesas.candybar.repositories.CustomerRepository;
import com.magicasprincesas.candybar.repositories.ReservationRepository;
import com.magicasprincesas.candybar.services.ReservationService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReservationImplService implements ReservationService {

  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;
  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  public ReservationImplService(ReservationRepository reservationRepository, ReservationMapper reservationMapper,
      CustomerRepository customerRepository, CustomerMapper customerMapper) {
    this.reservationRepository = reservationRepository;
    this.reservationMapper = reservationMapper;
    this.customerRepository = customerRepository;
    this.customerMapper = customerMapper;
  }

  @Override
  public ReservationResponseDto saveReservation(ReservationRequestDto requestDto) {
    LocalDate reservationDate = requestDto.getReservationDate();
    LocalDate currentDate = LocalDate.now();

    if (reservationDate.isBefore(currentDate)) {
      throw new CustomException("Reservation date cannot be in the past.");
    }

    if (reservationRepository.countByReservationDate(reservationDate) >= 3) {
      throw new CustomException("Cannot book more than 3 reservations per day.");
    }

    Customer customer = customerRepository
        .findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndPhone(
            requestDto.getFirstName(), requestDto.getLastName(), requestDto.getPhone())
        .orElseGet(() -> {
          validateCustomerData(requestDto);
          return customerRepository.save(customerMapper.toEntity(
              createCustomerDto(requestDto)));
        });

    updateCustomerPhoneIfNeeded(requestDto, customer);

    Reservation reservation = reservationMapper.toEntity(requestDto);
    reservation.setCustomer(customer);
    reservation = reservationRepository.save(reservation);

    return reservationMapper.toDto(reservation);
  }

  private void validateCustomerData(ReservationRequestDto requestDto) {
    if (requestDto.getFirstName() == null || requestDto.getLastName() == null) {
      throw new CustomException("First name and last name are required to create a customer.");
    }
  }

  private CustomerRequestDto createCustomerDto(ReservationRequestDto requestDto) {
    return new CustomerRequestDto(
        requestDto.getFirstName(),
        requestDto.getLastName(),
        requestDto.getPhone());
  }

  private void updateCustomerPhoneIfNeeded(ReservationRequestDto requestDto, Customer customer) {
    if (requestDto.getPhone() != null && !requestDto.getPhone().equals(customer.getPhone())) {
      customer.setPhone(requestDto.getPhone());
      customerRepository.save(customer);
    }
  }

  @Override
  public ReservationResponseDto getReservationById(Long id) {
    return reservationMapper.toDto(
        reservationRepository.findById(id)
            .orElseThrow(() -> new CustomException("Reservation not found")));
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
  @Transactional
  public ReservationResponseDto updateReservation(Long id, ReservationRequestDto request) {
    Reservation reservation = reservationRepository.findById(id)
        .orElseThrow(() -> new CustomException("Reservation not found with id: " + id));

    Customer customer = updateOrCreateCustomer(request, reservation.getCustomer());

    reservationMapper.updateReservationFromDto(request, reservation);
    reservation.setCustomer(customer);

    return reservationMapper.toDto(reservationRepository.save(reservation));
  }

  private Customer updateOrCreateCustomer(ReservationRequestDto request, Customer existingCustomer) {
    if (existingCustomer == null) {
      return customerRepository.save(
          customerMapper.toEntity(createCustomerDto(request)));
    } else {
      updateCustomerData(existingCustomer, request);
      return customerRepository.save(existingCustomer);
    }
  }

  private void updateCustomerData(Customer customer, ReservationRequestDto request) {
    if (!customer.getFirstName().equals(request.getFirstName())) {
      customer.setFirstName(request.getFirstName());
    }
    if (!customer.getLastName().equals(request.getLastName())) {
      customer.setLastName(request.getLastName());
    }
    if (!customer.getPhone().equals(request.getPhone())) {
      customer.setPhone(request.getPhone());
    }
  }

  @Override
  @Transactional
  public void deletePastReservations() {
    LocalDate currentDate = LocalDate.now();
    reservationRepository.deleteAll(
        reservationRepository.findByReservationDateBefore(currentDate));
  }

  @Override
  @Transactional
  public void deleteCustomersWithPastReservations() {
    LocalDate currentDate = LocalDate.now();
    List<Customer> customersToDelete = reservationRepository.findByReservationDateBefore(currentDate).stream()
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

    pastReservations.stream()
        .map(Reservation::getCustomer)
        .distinct()
        .filter(customer -> !hasFutureReservations(customer, currentDate))
        .forEach(customerRepository::delete);
  }

  private boolean hasFutureReservations(Customer customer, LocalDate currentDate) {
    return reservationRepository.findByCustomer(customer).stream()
        .anyMatch(reservation -> !reservation.getReservationDate().isBefore(currentDate));
  }

  @Override
  @Transactional
  public ReservationResponseDto recoverReservation(Long id) {
    Reservation reservation = reservationRepository.findByIdAndDeletedTrue(id)
        .orElseThrow(() -> new CustomException("Deleted reservation not found with id: " + id));

    reservation.setDeleted(false);
    return reservationMapper.toDto(reservationRepository.save(reservation));
  }
}
