package com.payment.repository;

import com.payment.entity.User;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

  default Specification<User> init() {
    return (root, query, builder) -> builder.isNotNull(root.get("id"));
  }

  default Specification<User> idIsNot(final Integer id) {
    if(Objects.nonNull(id)){
      return (root, query, builder) -> builder.notEqual(root.get("id"), id);
    }
    return init();
  }

  default Specification<User> email(final String email) {
    if(Objects.nonNull(email) && !email.isBlank()){
      return (root, query, builder) -> builder.equal(builder.upper(root.get("email")), email.toUpperCase());
    }
    return init();
  }

}
