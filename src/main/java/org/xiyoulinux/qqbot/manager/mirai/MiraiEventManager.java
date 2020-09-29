package org.xiyoulinux.qqbot.manager.mirai;

import com.alibaba.fastjson.JSONObject;
import kotlin.coroutines.CoroutineContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.TempMessageEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xiyoulinux.qqbot.annotation.Priority;
import org.xiyoulinux.qqbot.handle.mirai.message.MiraiMessageEventHandle;
import org.xiyoulinux.qqbot.manager.EventManager;
import org.xiyoulinux.qqbot.pojo.BootstrapContext;
import org.xiyoulinux.qqbot.pojo.mirai.EventHandleResult;
import org.xiyoulinux.qqbot.pojo.mirai.MiraiEventType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/18 上午11:32
 */
@Component
@Slf4j
public class MiraiEventManager extends EventManager {

    private final Map<MiraiEventType, List<MiraiMessageEventHandle<? extends MessageEvent>>> type2Handle = new HashMap<>();

    @Override
    public void registerEventHandle(BootstrapContext context) {
        log.info("register event handle, bootstrap: {}", context.getBootstrapService().getClass().getName());

        if (context.getArgs() == null || context.getArgs().length < 1 ||
                !(context.getArgs()[0] instanceof Bot)) {
            throw new IllegalArgumentException("miss bot object in context");
        }
        Bot bot = (Bot) context.getArgs()[0];

        Events.registerEvents(bot, new SimpleListenerHost() {
            @EventHandler
            public ListeningStatus onGroupMessage(GroupMessageEvent event) {
                return doHandle(MiraiEventType.MSG_GROUP, event);
            }

            @EventHandler
            public ListeningStatus onTempMessage(TempMessageEvent event) {
                return doHandle(MiraiEventType.MSG_TEMP, event);
            }

            @Override
            public void handleException(CoroutineContext context, Throwable exception) {
                throw new RuntimeException("在事件处理中发生异常", exception);
            }
        });
    }

    private <T extends MessageEvent> ListeningStatus doHandle(@NonNull MiraiEventType type, @NonNull T event) {
        List<MiraiMessageEventHandle<T>> handles = getEventHandles(type);
        for (MiraiMessageEventHandle<T> handler : handles) {
            try {
                EventHandleResult handleResult = handler.handle(event);
                if (EventHandleResult.isInstantResult(handleResult)) {
                    return handleResult.getStatus();
                }
            } catch (Exception exception) {
                log.error("event[{}] handle error, handle[{}]", type.name(), handler.name());
                log.error("", exception);
            }
        }
        return ListeningStatus.LISTENING;
    }

    private <T extends MessageEvent> List<MiraiMessageEventHandle<T>> getEventHandles(MiraiEventType type) {
        final List<MiraiMessageEventHandle<T>> eventHandles = new ArrayList<>();
        if (type == null) {
            throw new IllegalArgumentException("invalid mirai event type");
        }
        if (type2Handle.containsKey(type)) {
            List<MiraiMessageEventHandle<? extends MessageEvent>> handleList = type2Handle.get(type).stream()
                    .filter(Objects::nonNull).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(handleList)) {
                handleList.forEach(e -> {
                    //noinspection unchecked
                    eventHandles.add((MiraiMessageEventHandle<T>) e);
                });
            }
            return eventHandles;
        }

        Map<String, ? extends MiraiMessageEventHandle<? extends MessageEvent>> beanToHandles =
                applicationContext.getBeansOfType(type.getEventHandle());
        if (beanToHandles.isEmpty()) {
            type2Handle.put(type, Collections.emptyList());
            return Collections.emptyList();
        }

        @SuppressWarnings("rawtypes")
        List<AbstractMap.SimpleEntry<String, Class>> bean2Cls = beanToHandles
                .entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), (Class) (e.getValue().getClass())))
                .sorted(Comparator.comparingInt(e -> Optional.ofNullable(e.getValue().getAnnotation(Priority.class))
                        .map(p -> ((Priority) p).level())
                        .orElse(Integer.MAX_VALUE))
                )
                .collect(Collectors.toList());

        //noinspection unchecked
        bean2Cls.forEach(e -> eventHandles.add((MiraiMessageEventHandle<T>) beanToHandles.get(e.getKey())));

        log.info("scan event handles: {}", JSONObject.toJSONString(
                eventHandles.stream()
                        .map(MiraiMessageEventHandle::name)
                        .collect(Collectors.toList())
                )
        );
        type2Handle.put(type, new ArrayList<>(eventHandles));

        return eventHandles;
    }

}
