package org.xiyoulinux.qqbot.framework.annotation;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 优先级
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/9/17 下午6:09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Indexed
public @interface Priority {
    /**
     * 优先级定义 值越高优先级越低
     */
    int level() default Integer.MAX_VALUE;
}
