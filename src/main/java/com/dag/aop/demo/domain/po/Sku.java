package com.dag.aop.demo.domain.po;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sku {
    private String id;

    private String name;

    private Integer price;

    private Integer num;

    private String image;

    private String categoryName;

    private String brandName;

    private String spec;

    private Integer saleNum;

    public static void main(String[] args) {
        Sku sku = Sku.builder()
                .name("xiaomi 13")
                .categoryName("xiaomi")
                .build();
        System.out.println(JSON.toJSONString(sku));

    }
}