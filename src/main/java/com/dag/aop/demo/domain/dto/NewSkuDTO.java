package com.dag.aop.demo.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewSkuDTO extends SkuDTO {
  private String property;

  public static void main(String[] args) {
    NewSkuDTO skuDTO = new NewSkuDTO();
  }
}
