package com.payment;

import com.payment.entity.Account;
import com.payment.exception.BusinessException;
import com.payment.exception.ResourceNotFoundException;
import com.payment.model.PaymentInput;
import com.payment.model.PaymentOutput;
import com.payment.model.PaymentProductInput;
import com.payment.repository.AccountRepository;
import com.payment.repository.ProductRepository;
import com.payment.repository.UserRepository;
import com.payment.service.PaymentConfirmService;
import com.payment.service.PaymentCreateService;
import com.payment.service.PaymentHashDecoderService;
import com.payment.service.PaymentHashGenerateService;
import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class PaymentServiceIT {
  @Autowired
  private PaymentCreateService paymentCreateService;
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private PaymentHashGenerateService paymentHashGenerateService;
  @Autowired
  private PaymentHashDecoderService paymentHashDecoderService;
  @Autowired
  private PaymentConfirmService paymentConfirmService;

  @Test
  void paymentCreateService_paymentConfirmService_SuccessTest() {
    val input = new PaymentInput();

    val user = userRepository.findAll().get(0);
    input.setUser(user.getId());

    val account = accountRepository.findAll().get(0);
    input.setAccount(account.getId());

    val products = productRepository.findAll(productRepository.account(account)).stream().map(
      product -> {
        val productInput = new PaymentProductInput();
        productInput.setId(product.getId());
        productInput.setQuantity(new Random().nextInt(10 - 1) + 1);
        return productInput;
      }
    ).collect(Collectors.toList());
    input.setProducts(products);

    val payment = (PaymentOutput) paymentCreateService.execute(input).getContent();
    Assertions.assertNotNull(payment);
    Assertions.assertNotNull(payment.getHash());
    Assertions.assertTrue(payment.getTotalPrice().compareTo(BigDecimal.ZERO) > 0);
    Assertions.assertFalse(payment.isConfirmed());
    Assertions.assertEquals(payment.getHash(), paymentHashGenerateService.execute(1));

    val paymentConfirmed = (PaymentOutput) paymentConfirmService.hash(payment.getHash()).getContent();
    Assertions.assertTrue(paymentConfirmed.isConfirmed());
  }

  @Test
  void paymentCreateService_NoValidUser_FailTest() {
    val input = new PaymentInput();

    input.setUser(0);

    val account = accountRepository.findAll().get(0);
    input.setAccount(account.getId());

    Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> paymentCreateService.execute(input));
  }

  @Test
  void paymentCreateService_NoValidAccount_FailTest() {
    val input = new PaymentInput();
    input.setUser(1);
    input.setAccount(0);

    val user = userRepository.findAll().get(0);
    input.setUser(user.getId());

    input.setAccount(0);

    Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> paymentCreateService.execute(input));
  }

  @Test
  void paymentCreateService_FailProductsAndAccountDoNotMatch_FailTest() {
    val input = new PaymentInput();
    input.setUser(1);
    input.setAccount(1);

    val products = productRepository.findAll(productRepository.account(new Account(2))).stream().map(
      product -> {
        val productInput = new PaymentProductInput();
        productInput.setId(product.getId());
        productInput.setQuantity(new Random().nextInt(10 - 1) + 1);
        return productInput;
      }
    ).collect(Collectors.toList());
    input.setProducts(products);

    Assertions.assertThrowsExactly(BusinessException.class, () -> paymentCreateService.execute(input));
  }

  @Test
  public void paymentHashGenerateAndDecoderTest() {
    val randomId = new Random().nextInt();
    Assertions.assertEquals(randomId, paymentHashDecoderService.execute(paymentHashGenerateService.execute(randomId)));
  }

}
