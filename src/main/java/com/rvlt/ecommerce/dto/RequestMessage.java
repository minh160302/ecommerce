package com.rvlt.ecommerce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Getter
@Setter
@NoArgsConstructor
public class RequestMessage<T> {
  private Date time;
  private T data;

  // expected date format: 2024-07-12T21:40:03.421+00:00
  public void setTime(String time) throws ParseException {
//    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));   // This line converts the given date into UTC time zone
//    this.time = sdf.parse(time);
    this.time = new Date();
  }
}
