package org.xiyoulinux.qqbot.framework.handle.mirai.message.enhance;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.MessageEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;
import org.xiyoulinux.qqbot.framework.handle.mirai.EventHandleResult;
import org.xiyoulinux.qqbot.framework.handle.mirai.message.MiraiMessageEventHandle;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 进一步抽象，添加过滤机制，隐藏属于 Mirai 的操作，复杂操作需要实现
 *  <li>{@link MiraiMessageEventHandle}</li>
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/26 下午4:41
 */
@Slf4j
public abstract class BaseMiraiMessageEventHandle<T extends MessageEvent>
        implements MiraiMessageEventHandle<T> {

    /*
     * Group filter
     */

    protected Long[] allowGroups() {
        return new Long[0];
    }

    protected Long[] blockGroups() {
        return new Long[0];
    }

    /**
     * 获取群组 id 的 Func
     */
    @Nullable
    abstract Function<T, List<Long>> groupIdFunc();

    protected final ListFilter<Long> groupFilter = ListFilter.of(null, null);

    /*
     * User filter
     */

    protected Long[] allowUsers() {
        return new Long[0];
    }

    protected Long[] blockUsers() {
        return new Long[0];
    }

    /**
     * 获取当前用户 QQ 号的 Func，默认返回发送者 QQ
     */
    @Nullable
    protected Function<T, List<Long>> userIdFunc() {
        return t -> Collections.singletonList(t.getSender().getId());
    }

    protected final ListFilter<Long> userFilter = ListFilter.of(null, null);

    /*
     * 自定义过滤 ------------------------------
     */

    @Nullable
    protected Filter<MessageContext> customFilter() {
        return null;
    }

    /*
     * 消息发送 ------------------------------
     */

    abstract void sendMessage(@NotNull T event, @NotNull ReplyContext replyContext);

    @NotNull
    protected abstract Function<MessageContext, ReplyContext> messageReply();

    @Override
    public EventHandleResult handle(T event) {
        Assert.notNull(event, "event cannot be null");
        final EventHandleResult defaultResult = EventHandleResult.DEFAULT_RESULT;

        MessageContext msgCtx = buildMsgCtx(event);

        // filter
        if (filter(event, groupIdFunc(), groupFilter, allowGroups(), blockGroups())
                || filter(event, userIdFunc(), userFilter, allowUsers(), blockUsers())) {
            return defaultResult;
        }
        if (Optional.ofNullable(customFilter()).map(filter -> filter.filter(msgCtx)).orElse(false)) {
            return defaultResult;
        }

        // doHandle
        log.info("do handler handle...");
        Function<MessageContext, ReplyContext> messageReplyFunc = messageReply();
        ReplyContext res = messageReplyFunc.apply(msgCtx);
        if (res == null || StringUtils.isBlank(res.getMessage())) {
            log.warn("message reply func return null");
            return defaultResult;
        }
        sendMessage(event, res);

        if (res.isProceed()) {
            return defaultResult;
        } else {
            return new EventHandleResult().setInstantResult(true);
        }
    }

    private MessageContext buildMsgCtx(T event) {
        return new MessageContext(chainString(event));
    }

    /**
     * 过滤返回 true
     */
    private <K> boolean filter(final T event,
                               @Nullable Function<T, List<K>> valFunc,
                               final ListFilter<K> listFilter,
                               @Nullable K[] allowList,
                               @Nullable K[] blockList) {
        return Optional.ofNullable(valFunc)
                .map(func -> func.apply(event))
                .map(vales -> listFilter.setAllowList(allowList).setBlockList(blockList).filter(vales))
                .orElse(false);
    }

}
