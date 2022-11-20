package com.payment.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "payments")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "id_user")
  private User user;

  @ManyToOne
  @JoinColumn(name = "id_account")
  private Account account;

  @OneToMany(mappedBy = "payment", cascade = CascadeType.PERSIST)
  private List<PaymentProduct> products;

  private BigDecimal totalPrice;

  private Boolean confirmed;

  @PrePersist
  public void prePersist() {
    try {
      this.confirmed = !Objects.isNull(this.confirmed) && this.confirmed;
      this.totalPrice = BigDecimal.ZERO;
      products.forEach(e -> {
        e.setPayment(this);
        totalPrice = totalPrice.add(e.getProduct().getPrice().multiply(BigDecimal.valueOf(e.getQuantity())));
      });
    } catch (final NullPointerException ignored) {
    }
  }

}
