package cn.edu.shu.pourrfot.cas.model.dto;

import cn.edu.shu.pourrfot.cas.enums.RoleEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "cn-edu-shu-pourrfot-server-model-dto-OauthAuthorizationCodeRequest")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthAuthorizationCodeRequest {
  @ApiModelProperty(required = true)
  @NotBlank
  private String username;
  @ApiModelProperty(required = true)
  @NotBlank
  private String password;
  @ApiModelProperty(example = "http://pourrfot-server.com/api", required = true)
  @NotNull
  private URL redirectUrl;
  @ApiModelProperty(example = "code", required = true)
  @NotBlank
  private String responseType;
  @ApiModelProperty(example = "pourrfot-web", required = true)
  @NotBlank
  private String clientId;
  @ApiModelProperty(required = true)
  @NotNull
  private RoleEnum scope;
  @ApiModelProperty(example = "state", required = true)
  @NotBlank
  private String state;
}
