package org.xiyoulinux.qqbot.handle;

import org.jetbrains.annotations.NotNull;
import org.xiyoulinux.qqbot.core.BootstrapContext;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/17 下午6:02
 */
public abstract class EventManager {

    /**
     * 注册事件 {@link org.xiyoulinux.qqbot.handle.EventHandle}
     *
     * @param context 启动上下文
     */
    public abstract void registerEventHandle(BootstrapContext context);

    @NotNull
    public abstract EventType getType();

    public static enum EventType {
        /**
         * 消息
         */
        MESSAGE;
    }

    public static EventType[] eventTypes() {
        return EventType.values();
    }

}
