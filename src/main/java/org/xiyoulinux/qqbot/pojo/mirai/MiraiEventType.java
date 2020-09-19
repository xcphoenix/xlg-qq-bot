package org.xiyoulinux.qqbot.pojo.mirai;

import lombok.Getter;
import net.mamoe.mirai.message.MessageEvent;
import org.xiyoulinux.qqbot.handle.mirai.GroupEventHandle;
import org.xiyoulinux.qqbot.handle.mirai.MiraiEventHandle;
import org.xiyoulinux.qqbot.handle.mirai.TempEventHandle;

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
    GROUP(GroupEventHandle.class),
    /**
     * 临时会话
     */
    TEMP(TempEventHandle.class);

    private final Class<? extends MiraiEventHandle<? extends MessageEvent>> eventHandle;

    MiraiEventType(Class<? extends MiraiEventHandle<? extends MessageEvent>> eventHandle) {
        this.eventHandle = eventHandle;
    }

}
