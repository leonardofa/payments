package com.payment.service;

import com.payment.entity.Payment;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentHashGenerateService {

  public String execute(final Payment payment) {
    return Base64.getEncoder().encodeToString(String.valueOf(payment.getId()).getBytes());
  }

}
