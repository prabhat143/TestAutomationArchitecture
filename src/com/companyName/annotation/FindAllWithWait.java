package com.companyName.annotation;

import com.companyName.enums.ScrollTypes;
import com.companyName.enums.WaitTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FindAllWithWait {
    String xpath() default "";
    String css() default "";
    String id() default "";
    String name() default "";
    String className() default "";
    String linkText() default "";
    String partialLinkText() default "";
    int waitTimeoutInSeconds() default 5;
    WaitTypes waitType() default WaitTypes.PRESENCE_OF_ELEMENT_ON_DOM;
    ScrollTypes scrollType() default ScrollTypes.NONE;
}