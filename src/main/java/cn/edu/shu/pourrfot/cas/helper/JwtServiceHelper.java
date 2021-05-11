package cn.edu.shu.pourrfot.cas.helper;

import cn.edu.shu.pourrfot.cas.model.PourrfotUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

/**
 * @author spencercjh
 */
@Slf4j
@UtilityClass
public class JwtServiceHelper {
  /**
   * parse User to key-value pairs
   *
   * @param user user
   * @return map
   */
  public static Map<String, Object> parseUserToPayload(PourrfotUser user) {
    final ObjectMapper objectMapper = new ObjectMapper();
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
