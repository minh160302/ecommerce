package com.rvlt.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class JdbcConnection {
  @Value("spring.datasource.driver-class-name")
  private String DB_DRIVER_CLASS;

  @Value("spring.datasource.url")
  private String DB_URL;

  @Value("spring.datasource.username")
  private String DB_USERNAME;

  @Value("spring.datasource.password")
  private String DB_PASSWORD;

  public Connection getConnection() {
    Connection con = null;
    try {
      // load the Driver Class
      Class.forName(DB_DRIVER_CLASS);

      // create the connection now
      con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    } catch (ClassNotFoundException e) {
      System.out.println("ClassNotFound Exception: " + e.getMessage());
      e.printStackTrace();
    } catch (SQLException e) {
      System.out.println("SQL Exception: " + e.getMessage());
      e.printStackTrace();
    }
    return con;
  }
}
