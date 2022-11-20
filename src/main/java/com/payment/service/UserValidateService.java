package com.payment.service;

import com.payment.model.ValidationModel;
import com.payment.entity.User;
import com.payment.exception.BusinessException;
import com.payment.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidateService {

  private final UserRepository userRepository;

  public User execute(final User user) {
    if(user.getName().split(" ").length < 2){
      throw new BusinessException(
        "The user name must contais 2 parts.",
        "The user name must contais 2 parts. Exemple: John Doe",
        List.of(ValidationModel.builder().field("name").detail("small name").build()),
        user.getEmail()
      );
    }
    val spec = userRepository.idIsNot(user.getId()).and(userRepository.email(user.getEmail()));
    if (userRepository.findOne(spec).isPresent()) {
      throw new BusinessException(
        "Email used already",
        String.format("The email adress %s has been use.", user.getEmail()),
        List.of(ValidationModel.builder().field("email").detail("used already").build()),
        user.getEmail()
      );
    }
    return user;
  }

}
