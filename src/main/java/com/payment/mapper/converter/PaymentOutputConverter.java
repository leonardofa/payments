package com.payment.mapper.converter;

import com.payment.entity.Payment;
import com.payment.model.PaymentOutput;
import com.payment.service.PaymentHashGenerateService;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentOutputConverter {

  private final ModelMapper mapper;
  private final ModelMapper intern = new ModelMapper();
  private final PaymentHashGenerateService paymentHashGenerateService;

  @PostConstruct
  public void init() {
    mapper.createTypeMap(Payment.class, PaymentOutput.class).setConverter(this::converter);
  }

  public PaymentOutput converter(MappingContext<Payment, PaymentOutput> context) {
    val input = context.getSource();
    val output = intern.map(context.getSource(), PaymentOutput.class);
    output.setHash(paymentHashGenerateService.execute(input));
    return output;
  }

}
