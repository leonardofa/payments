package com.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.model.PaymentInput;
import com.payment.model.PaymentOutput;
import com.payment.model.PaymentProductInput;
import com.payment.model.ResponseModel;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import static io.restassured.RestAssured.given;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentApiIT {

  @LocalServerPort
  private int port;
  private String hash;
  private final ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void setUpEach() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.port = port;
    RestAssured.basePath = "/api/payments";
  }

  /**
   * Payment flux
   */
  @Test
  void should_Payment_CreateAndConfirm_andFailAlreadyConfirm() throws JsonProcessingException {
    val input = new PaymentInput();
    input.setUser(1);
    input.setAccount(1);
    input.setProducts(IntStream.of(1, 2).mapToObj(produto -> {
      val paymentProduto = new PaymentProductInput();
      paymentProduto.setId(produto);
      paymentProduto.setQuantity(new Random().nextInt(10 - 1) + 1);
      return paymentProduto;
    }).collect(Collectors.toList()));
    val response = given().contentType(ContentType.JSON).accept(ContentType.JSON).when().body(new ObjectMapper().writeValueAsString(input))
      .post();
    response.then().statusCode(HttpStatus.CREATED.value());
    hash = mapper.readValue(mapper.writeValueAsString(response.body().as(ResponseModel.class).getContent()), PaymentOutput.class).getHash();
    given().accept(ContentType.JSON).put("/confirm/{hash}", hash).then().statusCode(HttpStatus.OK.value());
    given().accept(ContentType.JSON).put("/confirm/{hash}", hash).then().statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void should_Payment_FailValidations() throws JsonProcessingException {
    val input = new PaymentInput();
    given().contentType(ContentType.JSON).accept(ContentType.JSON).when().body(new ObjectMapper().writeValueAsString(input))
      .post().then().statusCode(HttpStatus.BAD_REQUEST.value()).body("validations", Matchers.hasSize(3));
  }

}
