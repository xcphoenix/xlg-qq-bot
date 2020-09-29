package org.xiyoulinux.qqbot.handler.online;

import net.mamoe.mirai.message.TempMessageEvent;
import org.springframework.stereotype.Component;
import org.xiyoulinux.qqbot.handle.mirai.message.TempMessageEventHandle;
import org.xiyoulinux.qqbot.pojo.mirai.EventHandleResult;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/19 下午5:23
 */
@Component
public class OnlineInfoTempHandle implements TempMessageEventHandle {

    private final OnlineService onlineService;

    public OnlineInfoTempHandle(OnlineService onlineService) {
        this.onlineService = onlineService;
    }

    @Override
    public EventHandleResult handle(TempMessageEvent event) {
        String msg = chainString(event);
        onlineService.onlineHandle(msg, replyMsg -> event.getSender().sendMessage(replyMsg));
        return EventHandleResult.DEFAULT_RESULT;
    }

}
