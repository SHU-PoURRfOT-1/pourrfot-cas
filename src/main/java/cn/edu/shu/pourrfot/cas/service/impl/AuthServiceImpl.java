package cn.edu.shu.pourrfot.cas.service.impl;

import cn.edu.shu.pourrfot.cas.exception.OauthAuthorizationException;
import cn.edu.shu.pourrfot.cas.model.PourrfotUser;
import cn.edu.shu.pourrfot.cas.repository.PourrfotUserMapper;
import cn.edu.shu.pourrfot.cas.service.AuthService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author spencercjh
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
  private static final String OAUTH_PREFIX = "cas:oauth:";
  @Value("${cas.redis.key}")
  String redisMapCacheKey;
  @Autowired
  private PourrfotUserMapper pourrfotUserMapper;
  @Autowired
  private RedissonClient redissonClient;
  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public PourrfotUser login(String username, String password) {
    final PourrfotUser found = pourrfotUserMapper.selectOne(Wrappers.query(new PourrfotUser())
      .eq(PourrfotUser.COL_USERNAME, username));
    if (found == null || !password.equals(found.getPassword())) {
      return null;
    }
    return found;
  }

  @Override
  public String generateAuthorizationCode(PourrfotUser user) {
    String userJsonString;
    try {
      userJsonString = objectMapper.writeValueAsString(user);
    } catch (JsonProcessingException e) {
      log.error("Serialize PourrfotUser Json failed", e);
      throw new OauthAuthorizationException(String.format("Serialize PourrfotUser Json failed: %s",
        StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : e.toString()));
    }
    final String code = UUID.randomUUID().toString().replaceAll("-", "");
    try {
      final RMapCache<String, String> mapCache = redissonClient.getMapCache(redisMapCacheKey);
      mapCache.put(OAUTH_PREFIX + code, userJsonString, 60, TimeUnit.SECONDS);
      return code;
    } catch (Exception e) {
      log.error("OAuth2.0 redis set code error, user: {}, code: {} because: {}", user, code,
        StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : e.toString(), e);
    }
    throw new OauthAuthorizationException(String.format("OAuth2.0 redis set code error: %s", code));
  }

  @Override
  public PourrfotUser getUserByAuthorizationCode(String authorizationCode) {
    if (StringUtils.isBlank(authorizationCode)) {
      return null;
    }
    final RMapCache<String, String> mapCache = redissonClient.getMapCache(redisMapCacheKey);
    final String userJsonString = mapCache.get(OAUTH_PREFIX + authorizationCode);
    if (StringUtils.isBlank(userJsonString)) {
      return null;
    }
    try {
      return objectMapper.readValue(userJsonString, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      log.error("Deserialize PourrfotUser Json failed", e);
      throw new OauthAuthorizationException(String.format("Deserialize PourrfotUser Json failed: %s",
        StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : e.toString()));
    }
  }
}
