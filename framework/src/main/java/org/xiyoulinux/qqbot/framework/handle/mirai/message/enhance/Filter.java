package org.xiyoulinux.qqbot.framework.handle.mirai.message.enhance;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/12/1 下午1:04
 */
@FunctionalInterface
public interface Filter<K> {

    default boolean getValid() {
        return true;
    }

    boolean filterFunc(K k);

    default boolean filter(K k) {
        return getValid() && filterFunc(k);
    }

}
