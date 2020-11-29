package org.xiyoulinux.qqbot.handle.mirai.message;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import org.xiyoulinux.qqbot.handle.EventHandle;
import org.xiyoulinux.qqbot.handle.mirai.EventHandleResult;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/17 下午5:58
 */
public interface MiraiMessageEventHandle<T extends MessageEvent>
        extends EventHandle<T, EventHandleResult> {

    /**
     * chain to string
     *
     * @param chain ..
     * @return 消息内容
     */
    default @NotNull
    String chainString(@NotNull MessageChain chain) {
        return chain.contentToString();
    }

    /**
     * event to string
     *
     * @param event 消息事件
     * @return 消息内容
     */
    default @NotNull
    String chainString(@NotNull MessageEvent event) {
        try {
            return chainString(event.getMessage());
        } catch (Exception exception) {
            new RuntimeException("get message from event error, event: " + JSONObject.toJSONString(event), exception)
                    .printStackTrace();
        }
        return "";
    }

    @Override @NotNull
    default String name() {
        return "MESSAGE/" + EventHandle.super.name();
    }

}
