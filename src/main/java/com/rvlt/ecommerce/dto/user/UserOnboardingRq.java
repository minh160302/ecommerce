package com.rvlt.ecommerce.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserOnboardingRq {
  private String firstName;
  private String lastName;
  private String dob;
  private String email;
}
