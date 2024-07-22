package com.rvlt.ecommerce.constants;

public class Constants {
  public static final class SERVER_STATUS_CODE {
    public static final String SUCCESS = "1";
    public static final String FAILED = "0";
  }

  public static final class SESSION_STATUS {
    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";
  }

  public static final class ORDER_STATUS {
    public static final String NOT_SUBMITTED = "NOT_SUBMITTED";
    public static final String DELIVERED = "DELIVERED";
    public static final String DELIVERY_IN_PROGRESS = "DELIVERY_IN_PROGRESS";
    public static final String CANCELLED = "CANCELLED";
    public static final String PROCESSING_SUBMIT = "PROCESSING_SUBMIT";
    public static final String PROCESSING_CANCEL = "PROCESSING_CANCEL";
    public static final String RETURN_IN_PROGRESS = "RETURN_IN_PROGRESS";
    public static final String RETURNED = "RETURNED";

    //??
    public static final String DELIVERY_FAILED = "DELIVERY_FAILED";
    public static final String RETURN_FAILED = "RETURN_FAILED";
    public static final String CANCELLED_FAILED = "CANCELLED_FAILED";
  }

  public static final class CART_ACTIONS {
    public static final String ADD = "ADD";
    public static final String UPDATE = "UPDATE";
  }
}
