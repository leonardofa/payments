package com.payment.controller;

import com.payment.model.ResponseModel;
import com.payment.model.UserInput;
import com.payment.service.UserCreateService;
import com.payment.service.UserDeleteService;
import com.payment.service.UserRetriveService;
import com.payment.service.UserUpdateService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserCreateService userCreateService;
  private final UserUpdateService userUpdateService;
  private final UserDeleteService userDeleteService;
  private final UserRetriveService userRetriveService;

  @PostMapping
  public ResponseEntity<ResponseModel> create(@RequestBody @Valid UserInput input) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userCreateService.execute(input));
  }

  @PutMapping("{id}")
  public ResponseEntity<ResponseModel> update(@PathVariable Integer id, @RequestBody @Valid UserInput input) {
    return ResponseEntity.ok(userUpdateService.execute(id, input));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<ResponseModel> delete(@PathVariable Integer id) {
    return ResponseEntity.ok(userDeleteService.execute(id));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseModel> one(@PathVariable Integer id) {
    return ResponseEntity.ok(userRetriveService.execute(id));
  }

  @GetMapping
  public ResponseEntity<ResponseModel> all() {
    return ResponseEntity.ok().body(userRetriveService.execute());
  }

}
