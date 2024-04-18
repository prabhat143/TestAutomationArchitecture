package com.companyName.annotation;

import com.companyName.enums.ScrollTypes;
import com.companyName.enums.WaitTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FindByWithWait {
    String xpath() default "";
    String id() default "";
    String name() default "";
    String className() default "";
    String css() default "";
    String linkText() default "";
    String partialLinkText() default "";
    String tag() default "";
    WaitTypes waitType() default WaitTypes.PRESENCE_OF_ELEMENT_ON_DOM;
    int waitTimeoutInSeconds() default 10;
    ScrollTypes scrollType() default ScrollTypes.NONE;
}

