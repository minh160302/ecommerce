package com.rvlt.ecommerce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RequestMessage<T> {
  private Date time;
  private T data;

  // public void setTime(Date time) throws ParseException {
  //   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
  //   sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

  //   String utcTimeString = sdf.format(time);
  //   this.time = sdf.parse(utcTimeString);
  // }

  public void setTime(String time) throws ParseException {
//    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));   // This line converts the given date into UTC time zone
//    this.time = sdf.parse(time);
    this.time = new Date();
  }
}
