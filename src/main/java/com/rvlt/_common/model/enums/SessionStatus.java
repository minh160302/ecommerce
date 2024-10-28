package com.rvlt._common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SessionStatus {
  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE");

  private final String value;
}
