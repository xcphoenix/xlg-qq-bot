package org.xiyoulinux.qqbot.handle.mirai.message.enhance;

import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import org.jetbrains.annotations.Nullable;
import org.xiyoulinux.qqbot.handle.mirai.message.GroupMessageEventHandle;

import java.util.function.Function;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/26 下午6:04
 */
public abstract class BaseGroupMessageEventHandle
        extends BaseMiraiMessageEventHandle<GroupMessageEvent>
        implements GroupMessageEventHandle {

    @Nullable
    @Override
    protected Function<GroupMessageEvent, Long> groupIdFunc() {
        return groupMessageEvent -> groupMessageEvent.getGroup().getId();
    }

    @Override
    void sendMessage(GroupMessageEvent event, String message, boolean isAt) {
        if (isAt) {
            event.getGroup().sendMessage(new At(event.getSender()).plus(message));
        } else {
            event.getGroup().sendMessage(message);
        }
    }

}
