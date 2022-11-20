package com.payment.controller;

import com.payment.model.ResponseModel;
import com.payment.service.AccountRetriveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountRetriveService accountRetriveService;

  @GetMapping("{id}")
  public ResponseEntity<ResponseModel> one(@PathVariable final Integer id) {
    return ResponseEntity.ok(accountRetriveService.execute(id));
  }

  @GetMapping
  public ResponseEntity<ResponseModel> all() {
    return ResponseEntity.ok(accountRetriveService.execute());
  }

}
