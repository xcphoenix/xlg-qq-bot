package org.xiyoulinux.qqbot.framework.handle.mirai.message.enhance;

import net.mamoe.mirai.message.TempMessageEvent;
import org.xiyoulinux.qqbot.framework.handle.mirai.message.TempMessageEventHandle;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/26 下午6:08
 */
public abstract class BaseTempMessageEventHandle extends BaseMiraiMessageEventHandle<TempMessageEvent>
        implements TempMessageEventHandle {

    @Override
    void sendMessage(TempMessageEvent event, String message, boolean isAt) {
        event.getSender().sendMessage(message);
    }

}
