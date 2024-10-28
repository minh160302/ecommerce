package com.rvlt._common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
  NOT_SUBMITTED("NOT_SUBMITTED"),
  PROCESSING_SUBMIT("PROCESSING_SUBMIT"),
  DELIVERY_IN_PROGRESS("DELIVERY_IN_PROGRESS"),
  DELIVERED("DELIVERED"),
  DELIVERY_FAILED("DELIVERY_FAILED"),
  PROCESSING_CANCEL("PROCESSING_CANCEL");

  private final String value;
}
