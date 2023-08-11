package com.dag.aop.demo.domain.po;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Integer id;

    private String userId;

    private String password;

    private String username;

    private String tel;
}