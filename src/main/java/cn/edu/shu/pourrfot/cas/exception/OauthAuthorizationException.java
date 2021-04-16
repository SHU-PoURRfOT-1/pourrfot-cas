package cn.edu.shu.pourrfot.cas.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author spencercjh
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "OAuth2.0 failed")
public class OauthAuthorizationException extends RuntimeException {
  private String message;
}
