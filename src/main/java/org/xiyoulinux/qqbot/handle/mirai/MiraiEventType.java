package org.xiyoulinux.qqbot.handle.mirai;

import lombok.Getter;
import net.mamoe.mirai.message.MessageEvent;
import org.xiyoulinux.qqbot.handle.mirai.message.GroupMessageEventHandle;
import org.xiyoulinux.qqbot.handle.mirai.message.MiraiMessageEventHandle;
import org.xiyoulinux.qqbot.handle.mirai.message.TempMessageEventHandle;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/18 上午11:57
 */
@Getter
public enum MiraiEventType {
    /**
     * 群组
     */
    MSG_GROUP(GroupMessageEventHandle.class),
    /**
     * 临时会话
     */
    MSG_TEMP(TempMessageEventHandle.class);

    private final Class<? extends MiraiMessageEventHandle<? extends MessageEvent>> eventHandle;

    MiraiEventType(Class<? extends MiraiMessageEventHandle<? extends MessageEvent>> eventHandle) {
        this.eventHandle = eventHandle;
    }

}
