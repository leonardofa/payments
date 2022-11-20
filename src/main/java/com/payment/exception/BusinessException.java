package com.payment.exception;

import com.payment.model.ValidationModel;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

  private final HttpStatus status;
  private final String title;
  private final String message;
  private final List<ValidationModel> validations;
  private final Object content;

  public BusinessException(final HttpStatus status, final String title, final String message) {
    super(title);
    this.status = status;
    this.title = title;
    this.message = message;
    this.validations = null;
    this.content = null;
  }

  public BusinessException(final String title, final String message) {
    super(title);
    this.status = HttpStatus.BAD_REQUEST;
    this.title = title;
    this.message = message;
    this.validations = null;
    this.content = null;
  }

  public BusinessException(final String title, final String message, final List<ValidationModel> validations, final Object content) {
    super(title);
    this.status = HttpStatus.BAD_REQUEST;
    this.title = title;
    this.message = message;
    this.validations = validations;
    this.content = content;
  }

}
