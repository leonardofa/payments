package com.payment.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentProductInput {
  @NotNull
  @Positive
  private Integer id;
  @NotNull
  @Positive
  private Integer quantity;
}
