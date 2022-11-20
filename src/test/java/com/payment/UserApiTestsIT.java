package com.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.model.UserInput;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import static io.restassured.RestAssured.given;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiTestsIT {

  @LocalServerPort
  private int port;

  @BeforeEach
  public void setUpEach() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.port = port;
    RestAssured.basePath = "/api/users";
  }

  @Test
  public void shouldReturnUsers_with200Code_WhenCallAllpathUser() {
    given().accept(ContentType.JSON).when().get()
      .then().statusCode(HttpStatus.OK.value());
  }

  @Test
  public void shouldReturnUserId1_WhenCallUserOne() {
    given().accept(ContentType.JSON).when().get("/{id}", 1)
      .then().body("content.name", CoreMatchers.equalTo("John Doe")).statusCode(HttpStatus.OK.value());
  }

  @Test
  public void shouldReturnNoUserInvalidId_with404Code_WhenCallUserOne() {
    given().accept(ContentType.JSON).when().get("/{id}", -1)
      .then().body("code", CoreMatchers.equalTo(404)).statusCode(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void shouldReturnNewUser_WhenCallUserCreate() throws JsonProcessingException {
    val user = new UserInput();
    user.setName("NEW USER");
    user.setEmail("teste_new@teste.com");
    given()
      .contentType(ContentType.JSON).accept(ContentType.JSON).when().body(new ObjectMapper().writeValueAsString(user)).post()
      .then().statusCode(HttpStatus.CREATED.value());
  }

}
