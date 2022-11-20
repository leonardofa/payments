package com.payment.service;

import com.payment.model.ProductOutput;
import com.payment.model.ResponseModel;
import com.payment.entity.Product;
import com.payment.exception.ResourceNotFoundException;
import com.payment.repository.ProductRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductRetriveService {

  private final ModelMapper mapper;
  private final ProductRepository productRepository;

  public ResponseModel execute(final Integer id) {
    return ResponseModel.builder().content(mapper.map(id(id), ProductOutput.class)).build();
  }

  public ResponseModel execute() {
    return ResponseModel.builder().content(productRepository.findAll().stream().map(e -> mapper.map(e, ProductOutput.class))
      .collect(Collectors.toList())).build();
  }

  public Product id(final Integer id) {
    return productRepository.findById(id).orElseThrow(() ->
      new ResourceNotFoundException(String.format("%s(%s)", Product.class.getSimpleName(), id), "Product not found")
    );
  }
}
