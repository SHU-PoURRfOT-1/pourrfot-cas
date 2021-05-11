package cn.edu.shu.pourrfot.cas.controller;

import cn.edu.shu.pourrfot.cas.model.dto.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author spencercjh
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/jwt")
public class JwtController {
  private final RsaJsonWebKey rsaJsonWebKey;

  public JwtController(@NotBlank @Value("${cas.jwk.keypair}") String rsaJsonWebKey,
                       @Autowired ObjectMapper objectMapper) throws Exception {
    this.rsaJsonWebKey = new RsaJsonWebKey(
      objectMapper.readValue(rsaJsonWebKey, new TypeReference<Map<String, Object>>() {
      })
    );
  }


  @GetMapping(value = "/public-key", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Result<Map<String, Object>>> getJwkPublicKey() {
    return ResponseEntity.ok(Result.normalOk("Get public-key success",
      rsaJsonWebKey.toParams(JsonWebKey.OutputControlLevel.PUBLIC_ONLY)));
  }
}
