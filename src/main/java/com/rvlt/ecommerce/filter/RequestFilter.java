package com.rvlt.ecommerce.filter;

import com.rvlt.ecommerce.constants.Constants;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.StringTokenizer;

@Component
@Order(1)
public class RequestFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, ResponseStatusException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }

  public static String getClientIpAddress(HttpServletRequest request) {
    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
    if (xForwardedForHeader == null) {
      return request.getRemoteAddr();
    } else {
      // As of https://en.wikipedia.org/wiki/X-Forwarded-For
      // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
      // we only want the client
      return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
    }
  }
}
