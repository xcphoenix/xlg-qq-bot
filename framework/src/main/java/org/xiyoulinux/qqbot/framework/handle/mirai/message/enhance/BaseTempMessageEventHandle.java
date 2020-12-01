package org.xiyoulinux.qqbot.framework.handle.mirai.message.enhance;

import net.mamoe.mirai.message.TempMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xiyoulinux.qqbot.framework.handle.mirai.message.TempMessageEventHandle;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/26 下午6:08
 */
public abstract class BaseTempMessageEventHandle extends BaseMiraiMessageEventHandle<TempMessageEvent>
        implements TempMessageEventHandle {

    @Nullable
    @Override
    Function<TempMessageEvent, List<Long>> groupIdFunc() {
        return tempMessageEvent -> Collections.singletonList(tempMessageEvent.getGroup().getId());
    }

    @Override
    void sendMessage(@NotNull TempMessageEvent event, @NotNull ReplyContext replyContext) {
        event.getSender().sendMessage(replyContext.getMessage());
    }

}
