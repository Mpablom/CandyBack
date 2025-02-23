package com.magicasprincesas.candybar.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.magicasprincesas.candybar.dtos.ReservationRequestDto;
import com.magicasprincesas.candybar.dtos.ReservationResponseDto;
import com.magicasprincesas.candybar.entities.Reservation;

@Mapper(componentModel = "spring", uses = CustomerMapper.class)
public interface ReservationMapper {

    @Mapping(target = "customer", source = ".")
    Reservation toEntity(ReservationRequestDto requestDto);

    @Mapping(target = "customer", source = "customer")
    ReservationResponseDto toDto(Reservation reservation);

    @Mapping(target = "customer", source = ".")
    void updateReservationFromDto(ReservationRequestDto dto, @MappingTarget Reservation entity);
}
