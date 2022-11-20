package com.payment.service;

import com.payment.model.AccountOutput;
import com.payment.model.ResponseModel;
import com.payment.entity.Account;
import com.payment.exception.ResourceNotFoundException;
import com.payment.repository.AccountRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountRetriveService {

  private final ModelMapper mapper;
  private final AccountRepository accountRepository;

  public Account id(final Integer id) {
    return accountRepository.findById(id).orElseThrow(() ->
      new ResourceNotFoundException(String.format("%s(%s)", Account.class.getSimpleName(), id), "Account not found")
    );
  }

  public ResponseModel execute(final Integer id) {
    return ResponseModel.builder().content(mapper.map(id(id), AccountOutput.class)).build();
  }

  public ResponseModel execute() {
    return ResponseModel.builder().content(accountRepository.findAll().stream().map(account -> mapper.map(account, AccountOutput.class))
      .collect(Collectors.toList())).build();
  }

}
