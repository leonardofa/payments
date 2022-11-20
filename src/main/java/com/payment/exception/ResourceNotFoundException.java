package com.payment.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {

  public ResourceNotFoundException(final String resource, final String message) {
    super(HttpStatus.NOT_FOUND, resource, message);
  }

}
