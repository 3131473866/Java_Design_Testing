package com.example.diaop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.diaop.services.Frontend*.*(..))" +
            " || execution(* com.example.diaop.services.Middleware*.*(..))" +
            " || execution(* com.example.diaop.services.Persistence*.*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logging Aspect: " + className + "." + methodName);
    }
}
