package com.payment.service;

import com.payment.entity.Payment;
import com.payment.exception.ResourceNotFoundException;
import com.payment.model.PaymentOutput;
import com.payment.model.ResponseModel;
import com.payment.repository.PaymentRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentRetriveService {

  private final ModelMapper mapper;
  private final PaymentRepository paymentRepository;
  private final PaymentHashDecoderService paymentHashDecoderService;

  public ResponseModel execute() {
    return ResponseModel.builder().content(paymentRepository.findAll().stream().map(e -> mapper.map(e, PaymentOutput.class))
      .collect(Collectors.toList())).build();
  }

  public Payment id(final Integer id) {
    return paymentRepository.findById(id).orElseThrow(() ->
      new ResourceNotFoundException(String.format("%s(%s)", Payment.class.getSimpleName(), id), "Payment not found")
    );
  }

  public Payment hash(final String hash) {
    return id(paymentHashDecoderService.execute(hash));
  }

}
