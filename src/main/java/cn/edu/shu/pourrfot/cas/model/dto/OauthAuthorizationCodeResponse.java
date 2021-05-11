package cn.edu.shu.pourrfot.cas.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author spencercjh
 */
@ApiModel(value = "cn-edu-shu-pourrfot-server-model-dto-OauthAuthorizationCodeResponse")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthAuthorizationCodeResponse {
  @ApiModelProperty()
  private String message;
  @ApiModelProperty()
  private String redirectUrl;
}
