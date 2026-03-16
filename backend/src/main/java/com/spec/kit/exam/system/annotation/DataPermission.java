package com.spec.kit.exam.system.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {
    String deptAlias() default "d";
    String userAlias() default "u";
    String permission() default "";
}