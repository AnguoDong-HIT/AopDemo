package com.dag.aop.demo.pojo;

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
}