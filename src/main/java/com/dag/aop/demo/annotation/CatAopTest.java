package com.dag.aop.demo.annotation;

import java.lang.annotation.*;

/**
 * @author: Gosin
 * @date: 2021/6/7 7:37 下午
 * @Description:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CatAopTest {
}
