package cn.edu.shu.pourrfot.cas.controller;

import cn.edu.shu.pourrfot.cas.helper.JwtServiceHelper;
import cn.edu.shu.pourrfot.cas.model.PourrfotUser;
import cn.edu.shu.pourrfot.cas.model.dto.*;
import cn.edu.shu.pourrfot.cas.service.AuthService;
import cn.edu.shu.pourrfot.cas.service.JwtService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.Map;

/**
 * @author spencercjh
 */
@Validated
@RestController
@RequestMapping("/oauth")
@Slf4j
public class OauthTokenController {
  private static final Map<String, String> CLIENT_SECRETS_MAP = Map.of("pourrfot-web", "123456");
  @Autowired
  private AuthService authService;
  @Autowired
  private JwtService jwtService;

  @PostMapping(value = "/code", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses({@ApiResponse(code = 403, message = "Wrong certification", response = Result.class),
    @ApiResponse(code = 200, message = "authorize successful, redirecting...", response = Result.class)})
  public ResponseEntity<Result<OauthAuthorizationCodeResponse>> authorize(@Valid @NotNull @RequestBody OauthAuthorizationCodeRequest oauthAuthorizationCodeRequest) {
    final String username = oauthAuthorizationCodeRequest.getUsername();
    final String password = oauthAuthorizationCodeRequest.getPassword();

    final PourrfotUser user = authService.login(username, password);
    return user == null || !user.getRole().equals(oauthAuthorizationCodeRequest.getScope()) ?
      ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(Result.of(HttpStatus.FORBIDDEN, "forbidden", OauthAuthorizationCodeResponse.builder()
          .message("Wrong certification or scope")
          .build())) :
      ResponseEntity.ok(Result.normalOk("success", OauthAuthorizationCodeResponse.builder()
        .message("authorize successful, redirecting...")
        .redirectUrl(setupRedirectedUrl(user, oauthAuthorizationCodeRequest.getRedirectUrl(), oauthAuthorizationCodeRequest.getState()))
        .build()));
  }

  private String setupRedirectedUrl(PourrfotUser user, URL redirectUrl, String state) {
    return UriComponentsBuilder.newInstance()
      .host(redirectUrl.getHost())
      .port(redirectUrl.getPort())
      .path(redirectUrl.getPath())
      .queryParam("code", authService.generateAuthorizationCode(user))
      .queryParam("state", state)
      .build().toString();
  }

  @PostMapping(value = "/token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Result<OauthTokenResponse>> createToken(@NotNull @Valid @RequestBody OauthTokenRequest oauthTokenRequest) {
    final String clientId = oauthTokenRequest.getClientId();
    final String clientSecret = oauthTokenRequest.getClientSecret();
    if (isIllegalClient(clientId, clientSecret)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(Result.of(HttpStatus.FORBIDDEN, "forbidden", OauthTokenResponse.builder()
          .message(String.format("Illegal client: %s:%s request Oauth2.0 /code:%s", clientId, clientSecret, oauthTokenRequest.getCode()))
          .build()));
    }
    final PourrfotUser user = authService.getUserByAuthorizationCode(oauthTokenRequest.getCode());
    if (user == null) {
      log.warn("[OAuth2] client request token can not find user by code, request: {}", oauthTokenRequest);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Result.of(HttpStatus.UNAUTHORIZED, "unauthorized", OauthTokenResponse.builder()
          .message("Can't find user by the code")
          .build()));
    }
    final JwtTokenData jwtTokenData = jwtService.generateToken(
      JwtServiceHelper.parseUserToPayload(user.setPassword("******")));
    return ResponseEntity.ok(Result.normalOk("success", OauthTokenResponse.builder()
      .token(jwtTokenData.getToken())
      .expireAt(jwtTokenData.getExpireAt())
      .user(user)
      .message("Oauth2.0 authorization success")
      .build()));
  }

  private boolean isIllegalClient(String clientId, String clientSecret) {
    return !CLIENT_SECRETS_MAP.getOrDefault(clientId, "").equals(clientSecret);
  }
}
