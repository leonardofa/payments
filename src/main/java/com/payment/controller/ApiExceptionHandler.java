package com.payment.controller;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.payment.model.ResponseModel;
import com.payment.model.ValidationModel;
import com.payment.exception.BusinessException;
import com.payment.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;
  private static final String DEFAUL_ERROR_MSG = "Try again. If the error runs again contact the support area.";

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUncaught(final Exception ex, final WebRequest request) {
    log.error("No expectation error", ex);
    return handleExceptionInternal(ex,
      builder(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "No expectation error occurred.",
        "No expectation error occurred.",
        DEFAUL_ERROR_MSG
      ),
      new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request
    );
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> handleEntidadeNaoEncontrada(final ResourceNotFoundException ex, final WebRequest request) {
    return handleExceptionInternal(
      ex, builder(ex.getStatus(), ex.getTitle(), ex.getMessage(), ex.getMessage()), new HttpHeaders(), ex.getStatus(), request
    );
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<?> handleNegocio(final BusinessException ex, final WebRequest request) {
    return handleExceptionInternal(
      ex,
      builder(ex.getStatus(), ex.getTitle(), ex.getMessage(), ex.getValidations(), ex.getContent()),
      new HttpHeaders(),
      ex.getStatus(),
      request
    );
  }

  @Override
  protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return getObjectResponseEntity(ex, headers, status, request);
  }

  private ResponseEntity<Object> getObjectResponseEntity(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    val validations = ex.getBindingResult().getAllErrors().stream().map(objectError -> {
      val name = objectError instanceof FieldError ? ((FieldError) objectError).getField() : objectError.getObjectName();
      return ValidationModel.builder().field(name).detail(messageSource.getMessage(objectError, LocaleContextHolder.getLocale())).build();
    }).collect(Collectors.toList());
    return handleExceptionInternal(ex, builder(validations, ex.getTarget()), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request
  ) {
    return getObjectResponseEntity(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
    final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request
  ) {
    return handleExceptionInternal(ex,
      builder(status, "Resource not found", String.format("The resource %s doesn't exist.", ex.getRequestURL()), DEFAUL_ERROR_MSG), headers,
      status, request
    );
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
    final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request
  ) {
    if (ex instanceof MethodArgumentTypeMismatchException) {
      return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
    }
    return super.handleTypeMismatch(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
    final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request
  ) {
    val rootCause = ExceptionUtils.getRootCause(ex);
    if (rootCause instanceof InvalidFormatException) {
      return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
    } else if (rootCause instanceof PropertyBindingException) {
      return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
    }
    val detail = "The request payload is incorrect. Verify it";
    return handleExceptionInternal(
      ex, builder(status, "Request is invalid", detail, DEFAUL_ERROR_MSG), headers, status, request
    );
  }

  private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
    final MethodArgumentTypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request
  ) {
    val detail = String.format(
      "The param '%s' was fill with an incorrect type ('%s'). The allowed type is %s.",
      ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()
    );
    return handleExceptionInternal(ex, builder(status, "Invalid param", detail, DEFAUL_ERROR_MSG), headers, status, request);
  }

  private ResponseEntity<Object> handlePropertyBinding(
    final PropertyBindingException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request
  ) {
    val detail = String.format("The field '%s' doesn't exist. Remove it and try again.", path(ex.getPath()));
    return handleExceptionInternal(ex, builder(status, "Field does not exist", detail, DEFAUL_ERROR_MSG), headers, status, request);
  }

  private ResponseEntity<Object> handleInvalidFormat(
    final InvalidFormatException ex,
    final HttpHeaders headers,
    final HttpStatus status,
    final WebRequest request
  ) {
    val detail = String.format("A propriedade '%s' recebeu o valor '%s' é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
      path(ex.getPath()), ex.getValue(), ex.getTargetType().getSimpleName()
    );
    return handleExceptionInternal(ex, builder(status, "Formato inválido", detail, DEFAUL_ERROR_MSG), headers, status, request);
  }

  private String path(final List<Reference> references) {
    return references.stream().map(Reference::getFieldName).collect(Collectors.joining("."));
  }

  private ResponseModel builder(final HttpStatus status, final String title, final String detail, final String userMessage) {
    return ResponseModel.builder().time(LocalDateTime.now()).code(status.value()).title(title).detail(detail).userMessage(userMessage).build();
  }

  private ResponseModel builder(final List<ValidationModel> validations, final Object content) {
    return ResponseModel.builder().time(LocalDateTime.now()).code(HttpStatus.BAD_REQUEST.value()).title("Validation")
      .detail("There are invalid fields.").userMessage("One or more fields are invalids. Fill out correct request and try again.")
      .validations(validations).content(content).build();
  }

  private ResponseModel builder(
    final HttpStatus status, final String detail, final String userMessage, final List<ValidationModel> validations, final Object content
  ) {
    return ResponseModel.builder().time(LocalDateTime.now()).code(status.value()).title("Error").detail(detail).userMessage(userMessage)
      .validations(validations).content(content).build();
  }

}
