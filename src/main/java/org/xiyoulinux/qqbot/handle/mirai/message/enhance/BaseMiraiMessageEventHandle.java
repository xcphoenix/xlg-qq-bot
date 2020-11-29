package org.xiyoulinux.qqbot.handle.mirai.message.enhance;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.MessageEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.xiyoulinux.qqbot.handle.mirai.EventHandleResult;
import org.xiyoulinux.qqbot.handle.mirai.message.MiraiMessageEventHandle;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/26 下午4:41
 */
@Slf4j
public abstract class BaseMiraiMessageEventHandle<T extends MessageEvent>
        implements MiraiMessageEventHandle<T> {

    /*
     * 号码匹配 ------------------------------
     */

    protected Long[] allowGroups() {
        return new Long[0];
    }

    protected Long[] blockGroups() {
        return new Long[0];
    }

    protected Long[] allowUsers() {
        return new Long[0];
    }

    protected Long[] blockUsers() {
        return new Long[0];
    }

    /**
     * 获取群组 id 的 Func，默认返回空
     */
    @Nullable
    protected Function<T, Long> groupIdFunc() {
        return null;
    }

    /**
     * 获取当前用户 QQ 号的 Func，默认返回发送者 QQ
     */
    @Nullable
    protected Function<T, Long> userIdFunc() {
        return t -> t.getSender().getId();
    }

    /*
     * 自定义过滤 ------------------------------
     */

    @Nullable
    protected Function<T, Boolean> customFilter() {
        return null;
    }

    /*
     * 消息发送 ------------------------------
     */

    abstract void sendMessage(T event, String message, boolean isAt);

    @Nullable
    protected Function<String, Pair<String, Boolean>> messageReply() {
        return null;
    }

    protected EventHandleResult messageHandle(T event) {
        log.warn("handler not implement handle, do nothing");
        return EventHandleResult.DEFAULT_RESULT;
    }

    @Override
    final public EventHandleResult handle(T event) {
        // filter
        if (Optional.ofNullable(customFilter()).map(filter -> filter.apply(event)).orElse(false)
                || filter(event, groupIdFunc(), allowGroups(), blockGroups())
                || filter(event, userIdFunc(), allowUsers(), blockUsers())
        ) {
            return EventHandleResult.DEFAULT_RESULT;
        }

        // doHandle
        Function<String, Pair<String, Boolean>> messageReplyFunc = messageReply();
        if (messageReplyFunc != null) {
            Pair<String, Boolean> res = messageReplyFunc.apply(chainString(event));
            if (res == null) {
                log.warn("message reply func return null");
            } else {
                sendMessage(event, res.getLeft(), res.getValue());
            }
            return EventHandleResult.DEFAULT_RESULT;
        }

        return messageHandle(event);
    }

    /**
     * 过滤返回 true
     */
    private <K> boolean filter(T event, Function<T, K> filter, final K[] allow, final K[] block) {
        if (event == null || (ArrayUtils.isEmpty(allow) && ArrayUtils.isEmpty(block))) {
            return false;
        }
        K thisValue = filter.apply(event);
        boolean flag = !ArrayUtils.isNotEmpty(allow);
        final K[] conditionArr = ArrayUtils.isNotEmpty(allow) ? allow : block;
        return flag && ArrayUtils.contains(conditionArr, thisValue);
    }

}
