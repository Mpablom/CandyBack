package com.magicasprincesas.candybar.dtos;

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
public class CustomerResponseDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String phone;
}
