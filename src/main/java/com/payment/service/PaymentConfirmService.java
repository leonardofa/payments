package com.payment.service;

import com.payment.model.PaymentOutput;
import com.payment.model.ResponseModel;
import com.payment.exception.BusinessException;
import com.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentConfirmService {

  private final PaymentRetriveService paymentRetriveService;
  private final PaymentRepository paymentRepository;
  private final ModelMapper mapper;

  public ResponseModel hash(final String hash) {
    val payment = paymentRetriveService.hash(hash);
    if (payment.getConfirmed()) {
      throw new BusinessException(
        "Payment confimed already",
        String.format("Payment %s has been confimed.", payment.getId()),
        null,
        hash
      );
    }
    payment.setConfirmed(Boolean.TRUE);
    return ResponseModel.builder().title("Payment confimed").detail(String.format("Payment %s has confimed", payment.getId()))
      .userMessage("Payment confimed").content(mapper.map(paymentRepository.save(payment), PaymentOutput.class)).build();
  }

}
