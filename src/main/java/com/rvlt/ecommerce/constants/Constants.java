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
    public static final String PROCESSING = "PROCESSING";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELLED = "CANCELLED";
  }

  public static final class CART_ACTIONS {
    public static final String ADD = "ADD";
    public static final String UPDATE = "UPDATE";
  }
}
