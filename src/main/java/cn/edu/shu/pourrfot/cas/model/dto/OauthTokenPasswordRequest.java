package cn.edu.shu.pourrfot.cas.model.dto;

import cn.edu.shu.pourrfot.cas.enums.GrantTypeEnum;
import cn.edu.shu.pourrfot.cas.enums.RoleEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author spencercjh
 */
@ApiModel(value = "cn-edu-shu-pourrfot-server-model-dto-OauthTokenPasswordRequest")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthTokenPasswordRequest {
  @ApiModelProperty(required = true)
  @NotBlank
  private String username;
  @ApiModelProperty(required = true)
  @NotBlank
  private String password;
  @ApiModelProperty(example = "pourrfot-web", required = true)
  @NotBlank
  private String clientId;
  @ApiModelProperty(example = "PASSWORD", required = true)
  @NotNull
  private GrantTypeEnum grantType;
}
