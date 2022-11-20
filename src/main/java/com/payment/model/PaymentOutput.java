package com.payment.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentOutput {

  private UserOutput user;
  private AccountOutput account;
  private List<PaymentProductOutput> products;
  private BigDecimal totalPrice;
  private boolean confirmed;
  private String hash;

}
