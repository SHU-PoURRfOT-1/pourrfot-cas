package cn.edu.shu.pourrfot.cas.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author spencercjh
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum SexEnum implements IEnum<String> {

  male("male"),
  female("female"),
  unknown("unknown");
  private final String sex;

  SexEnum(String sex) {
    this.sex = sex;
  }

  @Override
  public String getValue() {
    return this.sex;
  }
}
