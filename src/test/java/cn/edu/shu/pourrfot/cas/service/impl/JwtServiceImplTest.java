package cn.edu.shu.pourrfot.cas.service.impl;

import cn.edu.shu.pourrfot.cas.model.dto.JwtTokenData;
import cn.edu.shu.pourrfot.cas.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class JwtServiceImplTest {
  @Autowired
  private JwtService jwtService;

  @Test
  void generateToken() {
    final JwtTokenData result = jwtService.generateToken(Map.of("username", "spencercjh",
      "role", "student"));
    log.info(String.valueOf(result));
    assertTrue(StringUtils.isNotBlank(result.getToken()));
    assertTrue(result.getExpireAt() > 0);
  }
}
