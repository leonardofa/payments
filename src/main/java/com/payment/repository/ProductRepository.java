package com.payment.repository;

import com.payment.entity.Account;
import com.payment.entity.Product;
import com.payment.entity.User;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

  default Specification<Product> init() {
    return (root, query, builder) -> builder.isNotNull(root.get("id"));
  }

  default Specification<Product> id(final Integer id) {
    if(Objects.nonNull(id)){
      return (root, query, builder) -> builder.equal(root.get("id"), id);
    }
    return init();
  }

  default Specification<Product> account(final Account account) {
    if(Objects.nonNull(account)){
      return (root, query, builder) -> builder.equal(root.get("account"), account);
    }
    return init();
  }

}
