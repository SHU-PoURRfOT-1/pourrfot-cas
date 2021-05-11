package cn.edu.shu.pourrfot.cas.model.dto;

import cn.edu.shu.pourrfot.cas.model.PourrfotUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author spencercjh
 */
@ApiModel(value = "cn-edu-shu-pourrfot-server-model-dto-OauthTokenResponse")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthTokenResponse {
  @ApiModelProperty()
  private String token;
  @ApiModelProperty()
  private long expireAt;
  @ApiModelProperty()
  private PourrfotUser user;
  @ApiModelProperty()
  private String message;
}
