package org.xiyoulinux.qqbot.handle.mirai.message;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import org.xiyoulinux.qqbot.handle.EventHandle;
import org.xiyoulinux.qqbot.pojo.mirai.EventHandleResult;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/17 下午5:58
 */
public interface MiraiMessageEventHandle<T extends MessageEvent> extends EventHandle<T, EventHandleResult> {

    /**
     * chain to string
     *
     * @param chain ..
     * @return 消息内容
     */
    default @NotNull
    String chainString(MessageChain chain) {
        if (chain == null) {
            throw new IllegalArgumentException("chain can not be null");
        }
        return chain.contentToString();
    }

    /**
     * event to string
     *
     * @param event 消息事件
     * @return 消息内容
     */
    default @NotNull
    String chainString(MessageEvent event) {
        try {
            return chainString(event.getMessage());
        } catch (Exception exception) {
            new RuntimeException("get message from event error, event: " + JSONObject.toJSONString(event), exception)
                    .printStackTrace();
        }
        return "";
    }

}
