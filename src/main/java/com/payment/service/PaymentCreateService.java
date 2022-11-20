package com.payment.service;

import com.payment.entity.Payment;
import com.payment.entity.PaymentProduct;
import com.payment.model.PaymentInput;
import com.payment.model.PaymentOutput;
import com.payment.model.ResponseModel;
import com.payment.repository.PaymentRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCreateService {

  private final UserRetriveService userRetriveService;
  private final AccountRetriveService accountRetriveService;
  private final ProductRetriveService productRetriveService;
  private final PaymentValidateService paymentValidateService;
  private final PaymentRepository paymentRepository;
  private final ModelMapper mapper;

  public ResponseModel execute(final PaymentInput input) {
    val user = userRetriveService.id(input.getUser());
    val account = accountRetriveService.id(input.getAccount());
    paymentValidateService.execute(input);
    val payment = Payment.builder().user(user).account(account).products(
      input.getProducts().stream().map(
        product -> PaymentProduct.builder().quantity(product.getQuantity()).product(productRetriveService.id(product.getId())).build()
      ).collect(Collectors.toList())
    ).build();
    return ResponseModel.builder().content(mapper.map(paymentRepository.save(payment), PaymentOutput.class)).build();
  }

}
