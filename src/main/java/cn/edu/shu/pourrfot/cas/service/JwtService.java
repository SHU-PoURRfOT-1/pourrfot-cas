package cn.edu.shu.pourrfot.cas.service;

import cn.edu.shu.pourrfot.cas.model.JwtTokenData;
import cn.edu.shu.pourrfot.cas.model.PourrfotUser;
import org.jose4j.lang.JoseException;

import java.util.Map;

/**
 * @author spencercjh
 */
public interface JwtService {
  /**
   * generate JWT token
   *
   * @param payload payload
   * @return token and expire time
   */
  JwtTokenData generateToken(final Map<String, Object> payload);
}
