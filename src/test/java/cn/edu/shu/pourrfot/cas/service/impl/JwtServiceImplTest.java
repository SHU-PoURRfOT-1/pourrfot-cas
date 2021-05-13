package cn.edu.shu.pourrfot.cas.service.impl;

import cn.edu.shu.pourrfot.cas.enums.RoleEnum;
import cn.edu.shu.pourrfot.cas.helper.JwtServiceHelper;
import cn.edu.shu.pourrfot.cas.model.PourrfotUser;
import cn.edu.shu.pourrfot.cas.model.dto.JwtTokenData;
import cn.edu.shu.pourrfot.cas.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(JwtService.class)
@Slf4j
class JwtServiceImplTest {
  @Autowired
  private JwtService jwtService;

  @Test
  void generateToken() {
    final PourrfotUser mockAdmin = PourrfotUser.builder()
      .username("admin")
      .nickname("admin-test")
      .role(RoleEnum.admin)
      .password("admin")
      .build();
    final JwtTokenData jwtTokenData = jwtService.generateToken(
      JwtServiceHelper.parseUserToPayload(mockAdmin.setPassword("******")));
    log.info("JwtTokenData: {}", jwtTokenData);
  }
}
