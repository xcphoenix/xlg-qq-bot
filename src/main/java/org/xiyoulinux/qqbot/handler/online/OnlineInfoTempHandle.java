package org.xiyoulinux.qqbot.handler.online;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.xiyoulinux.qqbot.handle.mirai.message.enhance.BaseTempMessageEventHandle;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/19 下午5:23
 */
@Component
public class OnlineInfoTempHandle extends BaseTempMessageEventHandle {

    private final OnlineService onlineService;

    public OnlineInfoTempHandle(OnlineService onlineService) {
        this.onlineService = onlineService;
    }

    @Nullable
    @Override
    protected Function<String, Pair<String, Boolean>> messageReply() {
        AtomicReference<String> reply = new AtomicReference<>();
        return msg -> {
            onlineService.onlineHandle(msg, reply::set);
            return Pair.of(reply.get(), true);
        };
    }

    // @Override
    // public EventHandleResult handle(TempMessageEvent event) {
    //     String msg = chainString(event);
    //     onlineService.onlineHandle(msg, replyMsg -> event.getSender().sendMessage(replyMsg));
    //     return EventHandleResult.DEFAULT_RESULT;
    // }

}
