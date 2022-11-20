package com.payment.entity;

import javax.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "payments_products")
public class PaymentProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "id_payment")
  private Payment payment;

  @ManyToOne
  @JoinColumn(name = "id_product")
  private Product product;

  private Integer quantity;

}
