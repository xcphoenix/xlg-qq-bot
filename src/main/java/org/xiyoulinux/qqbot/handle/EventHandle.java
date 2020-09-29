package org.xiyoulinux.qqbot.handle;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/18 上午11:28
 */
public interface EventHandle<T, R> {

    /**
     * handle
     *
     * @param event 事件
     * @return 处理结果
     */
    R handle(T event);

    /**
     * 名称
     *
     * @return 事件处理器名称
     */
    default String name() {
        return this.getClass().getSimpleName();
    }

}
