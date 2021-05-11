package cn.edu.shu.pourrfot.cas.model.dto;

import cn.edu.shu.pourrfot.cas.model.PourrfotUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author spencercjh
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthTokenResponse {
  private String token;
  private long expireAt;
  private PourrfotUser user;
  private String message;
}
