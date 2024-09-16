package com.rvlt._common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BlogStatus {
  UNPUBLISHED("UNPUBLISHED"),
  WAITING_MOD_APPROVAL("WAITING_MOD_APPROVAL"),
  MOD_APPROVED("MOD_APPROVED"),
  MOD_SEND_BACK("MOD_SEND_BACK"), // ?
  MOD_REJECTED("MOD_REJECTED"),
  ADMIN_PUBLISHED("ADMIN_PUBLISHED"),
  ADMIN_REJECTED("ADMIN_REJECTED"),
  PUBLIC("PUBLIC");

  private final String value;
}
