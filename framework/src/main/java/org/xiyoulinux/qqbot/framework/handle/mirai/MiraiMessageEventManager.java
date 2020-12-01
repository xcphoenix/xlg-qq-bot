package org.xiyoulinux.qqbot.framework.handle.mirai;

import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.TempMessageEvent;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;
import org.xiyoulinux.qqbot.framework.annotation.Priority;
import org.xiyoulinux.qqbot.framework.core.BootstrapContext;
import org.xiyoulinux.qqbot.framework.core.MiraiBotData;
import org.xiyoulinux.qqbot.framework.handle.mirai.message.MiraiMessageEventHandle;
import org.xiyoulinux.qqbot.framework.handle.EventManager;
import org.xiyoulinux.qqbot.framework.utils.SpringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * TODO
 *  - 同优先级并发执行
 *  - handle执行添加超时处理
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/9/18 上午11:32
 */
@Slf4j
public class MiraiMessageEventManager extends EventManager {

    /**
     * handler超时时间（ms）
     */
    private String timeout;

    /**
     * monitor
     */
    private final Object monitor = new Object();

    /**
     * type to handle list
     */
    private Map<MiraiEventType, List<MiraiMessageEventHandle<? extends MessageEvent>>> type2Handle;

    @Override
    public void registerEventHandle(BootstrapContext context) {
        final MiraiBotData bot;
        try {
            bot = context.getArg(0, MiraiBotData.class, true);
        } catch (IllegalStateException ex) {
            throw new IllegalArgumentException("miss bot object in context", ex);
        }

        Events.registerEvents(bot.getImplBot(), new SimpleListenerHost() {
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
                log.error("event handle exec error", exception);
            }
        });
    }

    @NotNull
    @Override
    public EventType getType() {
        return EventType.MESSAGE;
    }

    protected <T extends MessageEvent> ListeningStatus doHandle(@NotNull MiraiEventType type,
                                                                @NotNull T event) {
        List<MiraiMessageEventHandle<T>> handles = getEventHandles(type);
        for (MiraiMessageEventHandle<T> handler : handles) {
            try {
                EventHandleResult handleResult = handler.handle(event);
                if (EventHandleResult.checkInstantResult(handleResult)) {
                    break;
                }
            } catch (Exception exception) {
                log.error("event[{}] handle error, handle[{}]", type.name(), handler.name());
                log.error("", exception);
            }
        }
        return ListeningStatus.LISTENING;
    }

    private <T extends MessageEvent> List<MiraiMessageEventHandle<T>> getEventHandles(@NotNull MiraiEventType type) {
        Assert.notNull(type, "invalid mirai event type");
        if (type2Handle == null || !type2Handle.containsKey(type)) {
            synchronized (monitor) {
                if (type2Handle == null) {
                    type2Handle = new ConcurrentHashMap<>();
                }
                if (!type2Handle.containsKey(type)) {
                    initEventHandles(type);
                }
            }
        }
        if (type2Handle.containsKey(type)) {
            // 这里如果发生了卸载，可能会获取不到
            List<MiraiMessageEventHandle<? extends MessageEvent>> handles = type2Handle.get(type);
            if (CollectionUtils.isNotEmpty(handles)) {
                List<MiraiMessageEventHandle<T>> eventHandles = new ArrayList<>();
                handles.forEach(handle -> {
                    if (type.getEventHandle().equals(handle.getClass()) ||
                            type.getEventHandle().isAssignableFrom(handle.getClass())) {
                        //noinspection unchecked
                        eventHandles.add((MiraiMessageEventHandle<T>) handle);
                    }
                });
                return eventHandles;
            }
        }

        return Collections.emptyList();
    }

    protected void initEventHandles(@NotNull MiraiEventType type) {
        List<MiraiMessageEventHandle<? extends MessageEvent>> eventHandles = new ArrayList<>();
        Map<String, ? extends MiraiMessageEventHandle<? extends MessageEvent>> beanToHandles =
                SpringUtils.getBeansOfType(type.getEventHandle());
        if (MapUtils.isEmpty(beanToHandles)) {
            type2Handle.put(type, new CopyOnWriteArrayList<>());
            return;
        }

        List<String> beanNameList = beanToHandles
                .entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().getClass()))
                .sorted(
                        Comparator.comparingInt(e -> Optional.ofNullable(e.getValue().getAnnotation(Priority.class))
                                .map(Priority::level)
                                .orElse(Integer.MAX_VALUE))
                )
                .map(AbstractMap.SimpleEntry::getKey)
                .collect(Collectors.toList());

        log.info("register {} handles: ", type.name());
        beanNameList.forEach(e -> {
            MiraiMessageEventHandle<? extends MessageEvent> handle = beanToHandles.get(e);
            eventHandles.add(handle);
            log.info(" - {}", handle.name());
        });

        // 使用线程安全的 CopyOnWriteArrayList 效率高，适用于读多写少的场景，此外由于读的是旧集合，实时性稍差
        type2Handle.put(type, new CopyOnWriteArrayList<>(eventHandles));
    }

}
