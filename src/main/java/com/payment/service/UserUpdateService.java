package com.payment.service;

import com.payment.entity.User;
import com.payment.model.ResponseModel;
import com.payment.model.UserInput;
import com.payment.model.UserOutput;
import com.payment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUpdateService {

  private final UserRetriveService userRetriveService;
  private final UserValidateService userValidateService;
  private final UserRepository userRepository;
  private final ModelMapper mapper;

  public ResponseModel execute(final Integer id, final UserInput input) {
    val user = userRetriveService.id(id);
    BeanUtils.copyProperties(mapper.map(input, User.class), user, "id");
    userValidateService.execute(user);
    return ResponseModel.builder().content(mapper.map(userRepository.save(user), UserOutput.class)).build();
  }

}
