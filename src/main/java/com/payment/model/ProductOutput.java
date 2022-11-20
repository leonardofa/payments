package com.payment.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductOutput {
  private Integer id;
  private String name;
  private BigDecimal price;
  private AccountOutput account;
}
