package com.MSGFCentralSys.MSGFCentralSys.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface BPMNSetterVariables {
    String[] variables() default "";
}
