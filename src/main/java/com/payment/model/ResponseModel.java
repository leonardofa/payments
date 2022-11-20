package com.payment.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel{
  @Builder.Default
  private final LocalDateTime time = LocalDateTime.now();
  @Builder.Default
  private final Integer code =  HttpStatus.OK.value();
  private final String title;
  private final String detail;
  private final String userMessage;
  private final List<ValidationModel> validations;
  private final Object content;
}
