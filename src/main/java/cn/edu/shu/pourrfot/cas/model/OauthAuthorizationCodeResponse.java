package cn.edu.shu.pourrfot.cas.model;

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
public class OauthAuthorizationCodeResponse {
  private String message;
  private String redirectUrl;
}
