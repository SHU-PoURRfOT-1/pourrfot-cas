package cn.edu.shu.pourrfot.cas.config;

import cn.edu.shu.pourrfot.cas.exception.OauthAuthorizationException;
import cn.edu.shu.pourrfot.cas.model.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author spencercjh
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(OauthAuthorizationException.class)
  protected ResponseEntity<Result<?>> handleResourceNotFoundException(OauthAuthorizationException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.of(HttpStatus.INTERNAL_SERVER_ERROR,
      StringUtils.isNotBlank(ex.getMessage()) ?
        ex.getMessage() : ex.toString()));
  }
}
