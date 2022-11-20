package com.payment.controller;

import com.payment.model.PaymentInput;
import com.payment.model.ResponseModel;
import com.payment.service.PaymentConfirmService;
import com.payment.service.PaymentCreateService;
import com.payment.service.PaymentRetriveService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentRetriveService paymentRetriveService;
  private final PaymentCreateService paymentCreateService;
  private final PaymentConfirmService paymentConfirmService;

  @GetMapping
  public ResponseEntity<ResponseModel> all() {
    return ResponseEntity.ok(paymentRetriveService.execute());
  }

  @PostMapping
  public ResponseEntity<ResponseModel> create(@RequestBody @Valid final PaymentInput input) {
    return ResponseEntity.status(HttpStatus.CREATED).body(paymentCreateService.execute(input));
  }

  @PutMapping("/confirm/{hash}")
  public ResponseEntity<ResponseModel> confirm(@PathVariable String hash) {
    return ResponseEntity.ok(paymentConfirmService.hash(hash));
  }

}
