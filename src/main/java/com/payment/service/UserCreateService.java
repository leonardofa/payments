package com.payment.service;

import com.payment.entity.User;
import com.payment.model.ResponseModel;
import com.payment.model.UserInput;
import com.payment.model.UserOutput;
import com.payment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCreateService {

  private final ModelMapper mapper;
  private final UserValidateService userValidateService;
  private final UserRepository userRepository;

  public ResponseModel execute(final UserInput input) {
    val user = mapper.map(input, User.class);
    userValidateService.execute(user);
    return ResponseModel.builder().content(mapper.map(userRepository.save(user), UserOutput.class)).build();
  }

}
