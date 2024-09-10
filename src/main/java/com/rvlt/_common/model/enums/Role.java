package com.rvlt._common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
  rvlt_admin("rvlt_admin"),
  rvlt_mod("rvlt_mod"),
  user("user");

  private final String value;
}
