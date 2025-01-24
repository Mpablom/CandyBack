package com.magicasprincesas.candybar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.magicasprincesas.candybar.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long>{ 
}
