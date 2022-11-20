package com.payment.service;

import com.payment.model.ResponseModel;
import com.payment.entity.User;
import com.payment.exception.UsingEntityException;
import com.payment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDeleteService {

  private final UserRetriveService userRetriveService;
  private final UserRepository userRepository;

  public ResponseModel execute(final Integer id) {
    try {
      userRepository.delete(userRetriveService.id(id));
    } catch (final DataIntegrityViolationException e) {
      throw new UsingEntityException(User.class.getSimpleName());
    }
    return ResponseModel.builder().title("Delete donne").detail(String.format("User delete (%s)", id)).userMessage("The user was delete").content(id)
      .build();
  }

}
