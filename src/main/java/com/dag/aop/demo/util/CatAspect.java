package com.dag.aop.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author : donganguo
 * @Date : 2021/6/7 8:02 下午
 * @Description :
 */

@Aspect
@Slf4j
@Component
public class CatAspect {

    private static final Logger logger = LoggerFactory.getLogger(CatAspect.class.getName());

    //声明切点
    @Pointcut("@annotation(com.dag.aop.demo.annotation.CatAopTest)")
    public void pointCut() {

    }

    //增强
    @Before("pointCut()")
    public Object before(JoinPoint jp) {
        String className = jp.getSignature().getDeclaringType().getSimpleName();
        String methodName = jp.getSignature().getName();
        logger.info(className + "::" + methodName);
        return null;
    }


}
