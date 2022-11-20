package com.payment.service;

import com.payment.model.PaymentInput;
import com.payment.model.ValidationModel;
import com.payment.entity.Account;
import com.payment.exception.BusinessException;
import com.payment.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentValidateService {

  private final ProductRepository productRepository;

  public void execute(final PaymentInput payment) {
    if (payment.getProducts().stream().anyMatch(product -> noExist(product.getId(), payment.getAccount()))) {
      throw new BusinessException(
        "Invalid product on list",
        "Invalid product on list",
        List.of(ValidationModel.builder().field("products").detail("account and products doesn't match").build()),
        payment
      );
    }
  }

  private boolean noExist(final Integer product, final Integer account) {
    return !productRepository.exists(productRepository.id(product).and(productRepository.account(new Account(account))));
  }

}
