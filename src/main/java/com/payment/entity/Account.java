package com.payment.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "accounts")
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private String iban;
  @ManyToOne
  @JoinColumn(name = "id_user")
  private User user;

  public Account(final Integer id) {
    this.id = id;
  }
}
