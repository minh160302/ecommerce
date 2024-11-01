package com.rvlt._common.constants;

public class Constants {
  public static final class SERVER_STATUS_CODE {
    public static final String SUCCESS = "1";
    public static final String FAILED = "0";
    public static final String SERVER_FAILED = "-100";
    public static final String MQ_ERROR = "-1";
  }

//  public static final class SESSION_STATUS {
//    public static final String ACTIVE = "ACTIVE";
//    public static final String INACTIVE = "INACTIVE";
//  }

//  public static final class ORDER_STATUS {
//    public static final String NOT_SUBMITTED = "NOT_SUBMITTED";
//    public static final String PROCESSING_SUBMIT = "PROCESSING_SUBMIT";
//    public static final String DELIVERED = "DELIVERED";
//    public static final String DELIVERY_IN_PROGRESS = "DELIVERY_IN_PROGRESS";
//    public static final String CANCELLED = "CANCELLED";
//    public static final String PROCESSING_CANCEL = "PROCESSING_CANCEL";
//    public static final String CANCEL_IN_PROGRESS = "CANCEL_IN_PROGRESS";
//    public static final String RETURN_IN_PROGRESS = "RETURN_IN_PROGRESS";
//    public static final String RETURNED = "RETURNED";
//
//    //??
//    public static final String DELIVERY_FAILED = "DELIVERY_FAILED";
//    public static final String RETURN_FAILED = "RETURN_FAILED";
//    public static final String CANCELLED_FAILED = "CANCELLED_FAILED";
//  }

  public static final class CART_ACTIONS {
    public static final String ADD = "ADD";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
  }

  public static final class WISHLIST_ACTIONS {
    public static final String ADD = "ADD";
    public static final String REMOVE = "REMOVE";
    public static final String CHECK = "CHECK";
  }

  public static final class RVLT {
    public static final String userIdHeader = "X-RVLT-userId";
    public static final String adminHeader = "rvlt_admin";
  }
}
