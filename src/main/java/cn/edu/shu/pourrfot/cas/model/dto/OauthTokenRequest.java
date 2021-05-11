package cn.edu.shu.pourrfot.cas.model.dto;

import cn.edu.shu.pourrfot.cas.enums.GrantTypeEnum;
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
@ApiModel(value = "cn-edu-shu-pourrfot-server-model-dto-OauthTokenRequest")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthTokenRequest {
  @ApiModelProperty(example = "AUTHORIZATION_CODE", required = true)
  @NotNull
  private GrantTypeEnum grantType;
  @ApiModelProperty(example = "pourrfot-web", required = true)
  @NotBlank
  private String clientId;
  @ApiModelProperty(example = "123456", required = true)
  @NotBlank
  private String clientSecret;
  @ApiModelProperty(required = true)
  @NotBlank
  private String code;
  @ApiModelProperty(example = "state", required = true)
  @NotBlank
  private String state;
}
