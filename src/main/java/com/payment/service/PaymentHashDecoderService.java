package com.payment.service;

import com.payment.exception.BusinessException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentHashDecoderService {

  public Integer execute(final String hash) {
    try {
      return Integer.valueOf(new String(Base64.getDecoder().decode(hash)));
    } catch (final Exception e) {
      throw new BusinessException("Invalid hash", String.format("Invalid hash: %s", hash));
    }
  }

}
