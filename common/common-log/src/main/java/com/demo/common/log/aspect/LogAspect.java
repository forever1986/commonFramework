package com.demo.common.log.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("execution(public * com.demo..*.controller..*.*(..)) || @annotation(com.demo.common.log.aspect.SysLog)") //controller包下的所有方法都打日剧
    public void logPointCut(){
    }

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint){
        log.info("方法执行前...");
        SysLog sysLog = getAnnotationLog(joinPoint);
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(sra != null){
            HttpServletRequest request = sra.getRequest();
            log.info("ip:" + request.getRemoteHost());
            log.info("url:" + request.getRequestURI());
            log.info("method:"+request.getMethod());
            log.info("class.method:" + joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
            log.info("args:" + joinPoint.getArgs());
            if(sysLog!=null){
                log.info(" 模块编码:"+sysLog.module().getCode());
                log.info(" 模块名称:"+sysLog.module().getName());
                log.info(" 方法描述:"+sysLog.description());
            }
        }
        log.info("----------AOP日志end---------------");
    }

    private SysLog getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return method != null ? method.getAnnotation(SysLog.class) : null;
    }
}
