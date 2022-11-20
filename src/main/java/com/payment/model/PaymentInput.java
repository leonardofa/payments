package com.payment.model;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInput {

  @NotNull
  @Positive
  private Integer user;

  @NotNull
  @Positive
  private Integer account;

  @Valid
  @NotEmpty
  private List<PaymentProductInput> products;

}
