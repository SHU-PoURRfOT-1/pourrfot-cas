package cn.edu.shu.pourrfot.cas.helper;

import cn.edu.shu.pourrfot.cas.model.PourrfotUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * @author spencercjh
 */
@Slf4j
@Component
public class JwtServiceHelper {
  private final ObjectMapper objectMapper;

  public JwtServiceHelper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * parse User to key-value pairs
   *
   * @param user user
   * @return map
   */
  public Map<String, Object> parseUserToPayload(PourrfotUser user) {
    try {
      final String userJsonString = objectMapper.writeValueAsString(user);
      return objectMapper.readValue(userJsonString, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      log.error("Serialize PourrfotUser Json failed", e);
      return Collections.emptyMap();
    }
  }
}
