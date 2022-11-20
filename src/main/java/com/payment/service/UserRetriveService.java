package com.payment.service;

import com.payment.model.ResponseModel;
import com.payment.model.UserOutput;
import com.payment.entity.User;
import com.payment.exception.ResourceNotFoundException;
import com.payment.repository.UserRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRetriveService {

  private final ModelMapper mapper;
  private final UserRepository userRepository;

  public User id(final Integer id) {
    return userRepository.findById(id).orElseThrow(() ->
      new ResourceNotFoundException(String.format("%s(%s)", User.class.getSimpleName(), id), "User not found")
    );
  }

  public ResponseModel execute(final Integer id) {
    return ResponseModel.builder().content(mapper.map(id(id), UserOutput.class)).build();
  }

  public ResponseModel execute() {
    return ResponseModel.builder().content(userRepository.findAll().stream().map(e -> mapper.map(e, UserOutput.class))
      .collect(Collectors.toList())).build();
  }

}
