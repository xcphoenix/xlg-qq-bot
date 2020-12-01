package org.xiyoulinux.qqbot.framework.handle.mirai.message.enhance;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/12/1 下午3:30
 */
@Data
@Accessors(chain = true)
public class ReplyContext {

    /**
     * 回复的消息
     */
    private String message;

    /**
     * 回复的动作
     */
    private Action action;

    /**
     * 是否继续执行其他 handler
     */
    private boolean proceed = true;

    public static enum Action {
        /**
         * &#064;对方
         */
        AT,
        /**
         * 引用消息
         */
        QUOTE;
    }

}
