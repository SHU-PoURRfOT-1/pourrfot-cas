package cn.edu.shu.pourrfot.cas.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JwtController.class)
@AutoConfigureMockMvc(addFilters = false)
@Slf4j
class JwtControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  void getJwkPublicKey() throws Exception {
    mockMvc.perform(get("/jwt/public-key")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.code").value(200))
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.message").exists())
      .andExpect(jsonPath("$.data").isMap());
  }
}
