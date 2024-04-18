package com.companyName.annotation;

import com.companyName.enums.ScrollTypes;
import com.companyName.enums.WaitTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface iOSByWithWait {

    String xpath() default "";
    String id() default "";
    String accessibilityId() default "";
    int waitTimeoutInSeconds() default 10;
    WaitTypes waitType() default WaitTypes.PRESENCE_OF_ELEMENT_ON_DOM;
    ScrollTypes scrollType() default ScrollTypes.NONE;
}
