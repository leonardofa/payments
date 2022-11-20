package com.payment.exception;

import org.springframework.http.HttpStatus;

public class UsingEntityException extends BusinessException {

  public UsingEntityException(final String recurso) {
    super(HttpStatus.BAD_REQUEST, recurso, "This entity is using. Not allow delete it.");
  }

}
