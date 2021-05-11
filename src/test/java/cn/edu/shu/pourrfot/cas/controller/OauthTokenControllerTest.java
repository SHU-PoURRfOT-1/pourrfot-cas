package cn.edu.shu.pourrfot.cas.controller;

import cn.edu.shu.pourrfot.cas.enums.GrantTypeEnum;
import cn.edu.shu.pourrfot.cas.enums.RoleEnum;
import cn.edu.shu.pourrfot.cas.exception.OauthAuthorizationException;
import cn.edu.shu.pourrfot.cas.model.PourrfotUser;
import cn.edu.shu.pourrfot.cas.model.dto.JwtTokenData;
import cn.edu.shu.pourrfot.cas.model.dto.OauthAuthorizationCodeRequest;
import cn.edu.shu.pourrfot.cas.model.dto.OauthTokenPasswordRequest;
import cn.edu.shu.pourrfot.cas.model.dto.OauthTokenRequest;
import cn.edu.shu.pourrfot.cas.service.AuthService;
import cn.edu.shu.pourrfot.cas.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OauthTokenController.class)
@AutoConfigureMockMvc(addFilters = false)
@Slf4j
class OauthTokenControllerTest {

  @MockBean
  private AuthService authService;
  @MockBean
  private JwtService jwtService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void authorize() throws Exception {
    final PourrfotUser mockUser = PourrfotUser.builder()
      .username("mock")
      .nickname("mock")
      .id(100)
      .role(RoleEnum.student)
      .build();
    given(authService.login(anyString(), anyString())).willReturn(mockUser);
    final OauthAuthorizationCodeRequest request = OauthAuthorizationCodeRequest.builder()
      .username("mock")
      .password("mock")
      .redirectUrl(new URL("http://mock-server.com/api"))
      .responseType("code")
      .clientId("test")
      .scope(RoleEnum.student)
      .state("state")
      .build();
    mockMvc.perform(post("/oauth/code")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.code").value(200))
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.message").exists())
      .andExpect(jsonPath("$.data.message").exists())
      .andExpect(jsonPath("$.data.redirectUrl").exists());
  }

  @Test
  void authorizeFailed1() throws Exception {
    given(authService.login(anyString(), anyString())).willReturn(null);
    final OauthAuthorizationCodeRequest request = OauthAuthorizationCodeRequest.builder()
      .username("mock")
      .password("mock")
      .redirectUrl(new URL("http://mock-server.com/api"))
      .responseType("code")
      .clientId("test")
      .scope(RoleEnum.student)
      .state("state")
      .build();
    mockMvc.perform(post("/oauth/code")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.code").value(403))
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void authorizeFailed2() throws Exception {
    final PourrfotUser mockUser = PourrfotUser.builder()
      .username("mock")
      .nickname("mock")
      .id(100)
      .role(RoleEnum.student)
      .build();
    given(authService.login(anyString(), anyString())).willReturn(mockUser);
    final OauthAuthorizationCodeRequest request = OauthAuthorizationCodeRequest.builder()
      .username("mock")
      .password("mock")
      .redirectUrl(new URL("http://mock-server.com/api"))
      .responseType("code")
      .clientId("pourrfot-web")
      .scope(RoleEnum.admin)
      .state("state")
      .build();
    mockMvc.perform(post("/oauth/code")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.code").value(403))
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void authorizeFailed3() throws Exception {
    final PourrfotUser mockUser = PourrfotUser.builder()
      .username("mock")
      .nickname("mock")
      .id(100)
      .role(RoleEnum.student)
      .build();
    given(authService.login(anyString(), anyString())).willReturn(mockUser);
    given(authService.generateAuthorizationCode(any(PourrfotUser.class))).willThrow(OauthAuthorizationException.class);
    final OauthAuthorizationCodeRequest request = OauthAuthorizationCodeRequest.builder()
      .username("mock")
      .password("mock")
      .redirectUrl(new URL("http://mock-server.com/api"))
      .responseType("code")
      .clientId("test")
      .scope(RoleEnum.student)
      .state("state")
      .build();
    mockMvc.perform(post("/oauth/code")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.code").value(500))
      .andExpect(jsonPath("$.data").doesNotExist())
      .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void createToken() throws Exception {
    final PourrfotUser mockUser = PourrfotUser.builder()
      .username("mock")
      .nickname("mock")
      .id(100)
      .role(RoleEnum.student)
      .build();
    given(authService.getUserByAuthorizationCode(anyString())).willReturn(mockUser);
    given(jwtService.generateToken(anyMap())).willReturn(JwtTokenData.builder()
      .token("mock")
      .expireAt(System.currentTimeMillis())
      .build());
    final OauthTokenRequest request = OauthTokenRequest.builder()
      .code("mock")
      .clientSecret("123456")
      .clientId("pourrfot-web")
      .grantType(GrantTypeEnum.AUTHORIZATION_CODE)
      .state("state")
      .build();
    mockMvc.perform(post("/oauth/token")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.code").value(200))
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.message").exists())
      .andExpect(jsonPath("$.data.expireAt").exists())
      .andExpect(jsonPath("$.data.token").exists())
      .andExpect(jsonPath("$.data.expireAt").isNumber())
      .andExpect(jsonPath("$.data.user").value(mockUser));
  }

  @Test
  void createTokenFailed1() throws Exception {
    final OauthTokenRequest request = OauthTokenRequest.builder()
      .code("mock")
      .clientSecret("wrong")
      .clientId("pourrfot-web")
      .grantType(GrantTypeEnum.AUTHORIZATION_CODE)
      .state("state")
      .build();
    mockMvc.perform(post("/oauth/token")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.code").value(403))
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void createTokenFailed2() throws Exception {
    given(authService.getUserByAuthorizationCode(anyString())).willReturn(null);
    final OauthTokenRequest request = OauthTokenRequest.builder()
      .code("mock")
      .clientSecret("123456")
      .clientId("pourrfot-web")
      .grantType(GrantTypeEnum.AUTHORIZATION_CODE)
      .state("state")
      .build();
    mockMvc.perform(post("/oauth/token")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.code").value(401))
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void createTokenByPassword() throws Exception {
    final PourrfotUser mockUser = PourrfotUser.builder()
      .username("mock")
      .nickname("mock")
      .id(100)
      .role(RoleEnum.student)
      .build();
    given(authService.login(eq("mock"),eq("mock"))).willReturn(mockUser);
    given(jwtService.generateToken(anyMap())).willReturn(JwtTokenData.builder()
      .token("mock")
      .expireAt(System.currentTimeMillis())
      .build());
    final OauthTokenPasswordRequest request = OauthTokenPasswordRequest.builder()
      .username("mock")
      .password("mock")
      .scope(RoleEnum.student)
      .clientId("pourrfot-web")
      .grantType(GrantTypeEnum.PASSWORD)
      .build();
    mockMvc.perform(post("/oauth/password-token")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.code").value(200))
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.message").exists())
      .andExpect(jsonPath("$.data.expireAt").exists())
      .andExpect(jsonPath("$.data.token").exists())
      .andExpect(jsonPath("$.data.expireAt").isNumber())
      .andExpect(jsonPath("$.data.user").value(mockUser));
  }

  @Test
  void createTokenByPasswordFailed1() throws Exception{
    given(authService.login(eq("mock"),eq("mock"))).willReturn(null);
    final OauthTokenPasswordRequest request = OauthTokenPasswordRequest.builder()
      .username("mock")
      .password("mock")
      .scope(RoleEnum.student)
      .clientId("pourrfot-web")
      .grantType(GrantTypeEnum.PASSWORD)
      .build();
    mockMvc.perform(post("/oauth/password-token")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.code").value(403))
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void createTokenByPasswordFailed2() throws Exception{
    final PourrfotUser mockUser = PourrfotUser.builder()
      .username("mock")
      .nickname("mock")
      .id(100)
      .role(RoleEnum.student)
      .build();
    given(authService.login(eq("mock"),eq("mock"))).willReturn(mockUser);
    final OauthTokenPasswordRequest request = OauthTokenPasswordRequest.builder()
      .username("mock")
      .password("mock")
      .scope(RoleEnum.admin)
      .clientId("pourrfot-web")
      .grantType(GrantTypeEnum.PASSWORD)
      .build();
    mockMvc.perform(post("/oauth/password-token")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.code").value(403))
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.message").exists());
  }
}
