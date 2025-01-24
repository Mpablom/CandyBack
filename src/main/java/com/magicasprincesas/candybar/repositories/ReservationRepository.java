package com.magicasprincesas.candybar.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.magicasprincesas.candybar.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long>{
  @Query("SELECT r FROM Reservation r WHERE r.reservationDate = :date")
  List<Reservation> findByReservationDate(@Param("date") LocalDate date);
  
  @Query("SELECT COUNT(r) FROM Reservation r WHERE r.reservationDate = :date")
  int countByReservationDate(@Param("date") LocalDate date);

  @Query("SELECT r FROM Reservation r WHERE r.deleted = false")
   List<Reservation> findAllActiveReservations();

  List<Reservation> findByReservationDateBefore(LocalDate date);

  Optional<Reservation> findByIdAndDeletedTrue(Long id);
}
