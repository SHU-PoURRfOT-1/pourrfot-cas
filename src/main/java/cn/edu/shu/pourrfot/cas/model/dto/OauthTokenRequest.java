package cn.edu.shu.pourrfot.cas.model.dto;

import cn.edu.shu.pourrfot.cas.enums.GrantTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author spencercjh
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthTokenRequest {
  @NotNull
  private GrantTypeEnum grantType;
  @NotBlank
  private String clientId;
  @NotBlank
  private String clientSecret;
  @NotBlank
  private String code;
  @NotBlank
  private String state;
}
