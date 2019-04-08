package ai.hyacinth.core.service.web.support.errorhandler;

import ai.hyacinth.core.service.web.common.ServiceApiConstants;
import ai.hyacinth.core.service.web.common.ServiceApiErrorResponse;
import ai.hyacinth.core.service.web.common.ServiceApiException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
@RequestMapping(ServiceApiConstants.API_PREFIX)
public class ServiceControllerExceptionHandler {

  private static Logger logger = LoggerFactory.getLogger(ServiceControllerExceptionHandler.class);

  @ExceptionHandler(ServiceApiException.class)
  public ResponseEntity<ServiceApiErrorResponse> handle(
      ServiceApiException ex, HttpServletRequest servletRequest) {
    logger.error("handle ServiceApiException", ex);
    return toResponseEntity(ex, servletRequest);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ServiceApiErrorResponse> handle(
      HttpRequestMethodNotSupportedException ex, HttpServletRequest servletRequest) {
    return toResponseEntity(
        new ServiceApiException(ServiceApiCommonErrorCode.METHOD_NOT_ALLOWED), servletRequest);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ServiceApiErrorResponse> handle(
      MethodArgumentNotValidException ex, HttpServletRequest servletRequest) {

    Map<String, Object> errorDetails = new LinkedHashMap<>();
    BindingResult br = ex.getBindingResult();
    if (br.hasFieldErrors()) {
      errorDetails.put(
          "field",
          br.getFieldErrors().stream().map(FieldError::getField).collect(Collectors.toList()));
    }
    if (br.hasGlobalErrors()) {
      errorDetails.put(
          "global",
          br.getGlobalErrors().stream()
              .map(ObjectError::getObjectName)
              .collect(Collectors.toList()));
    }

    return toResponseEntity(
        new ServiceApiException(ServiceApiCommonErrorCode.REQUEST_VALIDATION_FAILED, errorDetails),
        servletRequest);
  }

  private ResponseEntity<ServiceApiErrorResponse> toResponseEntity(
      ServiceApiException ex, HttpServletRequest servletRequest) {
    return ResponseEntity.status(ex.getHttpStatusCode())
        .body(fillErrorAttributes(ex.getErrorResponse(), servletRequest));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ServiceApiErrorResponse> handle(
      Exception ex, HttpServletRequest servletRequest) {
    logger.error("handle Exception", ex);
    String errorDetails = ex.getMessage();
    return toResponseEntity(
        new ServiceApiException(ServiceApiCommonErrorCode.UNKNOWN_ERROR, errorDetails),
        servletRequest);
  }

  @Value("${spring.application.name}")
  private String applicationName;

  private ServiceApiErrorResponse fillErrorAttributes(
      ServiceApiErrorResponse errorResponse, HttpServletRequest servletRequest) {
    errorResponse.setPath(servletRequest == null ? "" : servletRequest.getRequestURI());
    errorResponse.setTimestamp(new Date());
    errorResponse.setService(applicationName);
    return errorResponse;
  }
}
