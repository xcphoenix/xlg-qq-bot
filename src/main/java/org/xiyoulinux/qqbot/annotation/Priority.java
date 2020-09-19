package org.xiyoulinux.qqbot.annotation;

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
     * 优先级定义 >0有效 同等优先级按照bean字典序依次注入，优先级越高越先注入
     */
    int level() default 0;
}
