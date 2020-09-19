package org.xiyoulinux.qqbot.handle.mirai;

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
public interface MiraiEventHandle<T extends MessageEvent> extends EventHandle {

    /**
     * handle
     *
     * @param event 事件
     * @return 处理结果
     */
    EventHandleResult handle(T event);

    /**
     * chain to string
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

    /**
     * 名称
     *
     * @return 事件处理器名称
     */
    default String name() {
        return this.getClass().getSimpleName();
    }

}
