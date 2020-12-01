package org.xiyoulinux.qqbot.framework.handle.mirai.message.enhance;

import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xiyoulinux.qqbot.framework.handle.mirai.message.GroupMessageEventHandle;

import java.util.Collections;
import java.util.List;
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
    protected Function<GroupMessageEvent, List<Long>> groupIdFunc() {
        return groupMessageEvent -> Collections.singletonList(groupMessageEvent.getGroup().getId());
    }

    @Override
    void sendMessage(@NotNull GroupMessageEvent event, @NotNull ReplyContext replyContext) {
        String message = replyContext.getMessage();
        if (replyContext.getAction() == null) {
            event.getGroup().sendMessage(message);
        }
        //noinspection SwitchStatementWithTooFewBranches
        switch (replyContext.getAction()) {
            case AT:
                event.getGroup().sendMessage(new At(event.getSender()).plus(message));
                break;
            default:
                throw new UnsupportedOperationException("unsupported action");
        }
    }

}
