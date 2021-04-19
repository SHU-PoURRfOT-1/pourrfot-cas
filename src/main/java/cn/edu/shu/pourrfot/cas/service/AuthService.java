package cn.edu.shu.pourrfot.cas.service;

import cn.edu.shu.pourrfot.cas.model.PourrfotUser;

/**
 * @author spencercjh
 */
public interface AuthService {
  /**
   * basic authentication for Oauth2.0
   *
   * @param username username
   * @param password password
   * @return complete user entity without password
   */
  PourrfotUser login(String username, String password);

  /**
   * generate authorization code for Oauth2.0
   *
   * @param user user
   * @return authorization code
   */
  String generateAuthorizationCode(PourrfotUser user);

  /**
   * get user by authorization code generated before
   *
   * @param authorizationCode authorization code
   * @return complete user entity without password
   */
  PourrfotUser getUserByAuthorizationCode(String authorizationCode);
}
