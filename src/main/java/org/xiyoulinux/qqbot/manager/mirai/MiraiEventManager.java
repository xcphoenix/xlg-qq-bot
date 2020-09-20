package org.xiyoulinux.qqbot.manager.mirai;

import com.alibaba.fastjson.JSONObject;
import kotlin.coroutines.CoroutineContext;
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
import org.xiyoulinux.qqbot.annotation.Priority;
import org.xiyoulinux.qqbot.handle.mirai.GroupEventHandle;
import org.xiyoulinux.qqbot.handle.mirai.MiraiEventHandle;
import org.xiyoulinux.qqbot.handle.mirai.TempEventHandle;
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

    private final Map<MiraiEventType, List<MiraiEventHandle<?>>> type2Handle = new HashMap<>();

    @Override
    public void registerEventHandle(BootstrapContext context) {
        log.info("register event handle, bootstrap: {}", context.getBootstrapService().getClass().getName());

        if (context.getArgs() == null || context.getArgs().length < 1 ||
                !(context.getArgs()[0] instanceof Bot)) {
            throw new IllegalArgumentException("miss bot object in context");
        }
        Bot bot = (Bot) context.getArgs()[0];

        // TODO 重构代码
        Events.registerEvents(bot, new SimpleListenerHost() {
            @EventHandler
            public ListeningStatus onGroupMessage(GroupMessageEvent event) {
                List<MiraiEventHandle<?>> handles = getEventHandles(MiraiEventType.GROUP);
                for (MiraiEventHandle<? extends MessageEvent> handler : handles) {
                    try {
                        EventHandleResult handleResult = ((GroupEventHandle) handler).handle(event);
                        if (EventHandleResult.isInstantResult(handleResult)) {
                            return handleResult.getStatus();
                        }
                    } catch (Exception exception) {
                        log.error("event[{}] handle error, handle[{}]", MiraiEventType.GROUP.name(),
                                handler.getClass().getSimpleName());
                        log.error("", exception);
                    }
                }
                return ListeningStatus.LISTENING;
            }

            @EventHandler
            public ListeningStatus onTempMessage(TempMessageEvent event) {
                List<MiraiEventHandle<?>> handles = getEventHandles(MiraiEventType.TEMP);
                for (MiraiEventHandle<? extends MessageEvent> handler : handles) {
                    try {
                        EventHandleResult handleResult = ((TempEventHandle) handler).handle(event);
                        if (EventHandleResult.isInstantResult(handleResult)) {
                            return handleResult.getStatus();
                        }
                    } catch (Exception exception) {
                        log.error("event[{}] handle error, handle[{}]", MiraiEventType.GROUP.name(),
                                handler.getClass().getSimpleName());
                        log.error("", exception);
                    }
                }
                return ListeningStatus.LISTENING;
            }

            @Override
            public void handleException(CoroutineContext context, Throwable exception) {
                throw new RuntimeException("在事件处理中发生异常", exception);
            }
        });
    }

    private List<MiraiEventHandle<?>> getEventHandles(MiraiEventType type) {
        if (type == null) {
            throw new IllegalArgumentException("invalid mirai event type");
        }
        if (type2Handle.containsKey(type)) {
            return type2Handle.get(type);
        }

        Map<String, ? extends MiraiEventHandle<? extends MessageEvent>> beanToHandles =
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

        List<MiraiEventHandle<?>> eventHandles = new ArrayList<>();
        bean2Cls.forEach(e -> eventHandles.add(beanToHandles.get(e.getKey())));

        log.info("scan event handles: {}", JSONObject.toJSONString(
                eventHandles.stream()
                        .map(MiraiEventHandle::name)
                        .collect(Collectors.toList())
                )
        );
        type2Handle.put(type, eventHandles);

        return eventHandles;
    }

}
