package org.xiyoulinux.qqbot.framework.handle.mirai.message.enhance;

import lombok.Data;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/12/1 下午3:37
 */
@Data
public class MessageContext {

    private String message;

    public MessageContext(String message) {
        this.message = message;
    }

}
