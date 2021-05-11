package cn.edu.shu.pourrfot.cas.model.dto;

import cn.edu.shu.pourrfot.cas.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URL;

/**
 * @author spencercjh
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthAuthorizationCodeRequest {
  @NotBlank
  private String username;
  @NotBlank
  private String password;
  @NotNull
  private URL redirectUrl;
  @NotBlank
  private String responseType;
  @NotBlank
  private String clientId;
  @NotNull
  private RoleEnum scope;
  @NotBlank
  private String state;
}
