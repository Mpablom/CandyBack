package com.magicasprincesas.candybar.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.magicasprincesas.candybar.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long>{ 
   Optional<Customer> findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndPhone(String firstName, String lastName, String phone);
}
