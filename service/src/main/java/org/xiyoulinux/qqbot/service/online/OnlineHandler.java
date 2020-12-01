package org.xiyoulinux.qqbot.service.online;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.xiyoulinux.qqbot.framework.handle.mirai.message.enhance.*;

import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/12/1 下午4:24
 */
@Component
public class OnlineHandler {

    private final OnlineService onlineService;

    private final Pattern rulePattern = Pattern.compile("^小组.*(没)?有.*人");

    private final Long[] allowGroups = new Long[]{
            948609238L,
            695747801L,
            603879279L
    };

    private final Filter<MessageContext> ruleFilter = messageContext -> {
        String msg = messageContext.getMessage();
        return StringUtils.isEmpty(msg) || !rulePattern.matcher(msg).find();
    };

    public OnlineHandler(OnlineService onlineService) {
        this.onlineService = onlineService;
    }

    @Component
    public class OnlineInfoGroupHandle extends BaseGroupMessageEventHandle {

        @Override
        protected Long[] allowGroups() {
            return allowGroups;
        }

        @Nullable
        @Override
        protected Filter<MessageContext> customFilter() {
            return ruleFilter;
        }

        @NotNull
        @Override
        protected Function<MessageContext, ReplyContext> messageReply() {
            return messageContext -> new ReplyContext()
                    .setMessage(onlineService.onlineHandle())
                    .setAction(ReplyContext.Action.AT);
        }

    }

    @Component
    public class OnlineInfoTempHandle extends BaseTempMessageEventHandle {

        @Override
        protected Long[] allowGroups() {
            return allowGroups;
        }

        @Nullable
        @Override
        protected Filter<MessageContext> customFilter() {
            return ruleFilter;
        }

        @NotNull
        @Override
        protected Function<MessageContext, ReplyContext> messageReply() {
            return messageContext -> new ReplyContext().setMessage(onlineService.onlineHandle());
        }

    }

}
