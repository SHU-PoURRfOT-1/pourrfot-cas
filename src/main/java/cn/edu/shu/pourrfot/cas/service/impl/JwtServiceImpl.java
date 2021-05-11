package cn.edu.shu.pourrfot.cas.service.impl;

import cn.edu.shu.pourrfot.cas.exception.OauthAuthorizationException;
import cn.edu.shu.pourrfot.cas.model.dto.JwtTokenData;
import cn.edu.shu.pourrfot.cas.service.JwtService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author spencercjh
 */
@Service
@Slf4j
@Validated
public class JwtServiceImpl implements JwtService {
  private final RsaJsonWebKey rsaJsonWebKey;

  @Value("${cas.jwk.expire}")
  private int expire;

  public JwtServiceImpl(@NotNull @Value("${cas.jwk.keypair}") String rsaJsonWebKey,
                        @Autowired ObjectMapper objectMapper) throws Exception {
    this.rsaJsonWebKey = new RsaJsonWebKey(
      objectMapper.readValue(rsaJsonWebKey, new TypeReference<Map<String, Object>>() {
      })
    );
  }

  @Override
  public JwtTokenData generateToken(final Map<String, Object> payload) {
    final NumericDate expireAt = NumericDate.now();
    expireAt.addSeconds(expire);
    return JwtTokenData.builder()
      .expireAt(expireAt.getValue())
      .token(generateCustomToken(payload, expireAt.getValue()))
      .build();
  }

  private String generateCustomToken(Map<String, Object> payload, long expireAt) {
    if (expireAt < NumericDate.now().getValue()) {
      log.error("generate token error, invalid params, payLoad: {}, expireAt: {}", payload, expireAt);
      throw new IllegalArgumentException("generate jwt token error, expireAt is invalid");
    }

    final JsonWebSignature jws = new JsonWebSignature();
    jws.setAlgorithmHeaderValue(rsaJsonWebKey.getAlgorithm());
    jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
    jws.setKey(rsaJsonWebKey.getRsaPrivateKey());

    final JwtClaims claims = new JwtClaims();
    if (!CollectionUtils.isEmpty(payload)) {
      payload.forEach(claims::setClaim);
    }

    claims.setExpirationTime(NumericDate.fromSeconds(expireAt));
    jws.setPayload(claims.toJson());
    try {
      return jws.getCompactSerialization();
    } catch (JoseException e) {
      log.error(e.getMessage(), e);
      throw new OauthAuthorizationException(e.getMessage());
    }
  }
}
