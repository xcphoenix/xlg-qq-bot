package org.xiyoulinux.qqbot.core;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 事件上下文
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/9/18 下午3:12
 */
public class BootstrapContext {

    /**
     * 启动服务
     */
    @Getter
    private final BootstrapService bootstrapService;

    /**
     * 上下文附加参数
     */
    private final List<Map.Entry<Object, Class<?>>> args = new ArrayList<>();

    public BootstrapContext(BootstrapService bootstrapService) {
        this.bootstrapService = bootstrapService;
    }

    public void putArg(@NotNull Object arg) {
        Assert.notNull(arg, "bootstrap context arg cannot be null");
        args.add(new AbstractMap.SimpleEntry<>(arg, arg.getClass()));
    }

    @Nullable
    private Map.Entry<Object, Class<?>> getArgEntry(int index) {
        Assert.isTrue(index >= 0, "index cannot be null");
        if (index >= args.size()) {
            return null;
        }
        return args.get(index);
    }

    public <T> T getArg(int index, Class<T> clazz, boolean npeCheck) {
        Assert.notNull(clazz, "arg clazz cannot be null");
        Map.Entry<Object, Class<?>> entry = getArgEntry(index);
        Assert.state(!npeCheck || entry != null, "arg cannot be null");
        if (entry == null) {
            return null;
        }
        if (entry.getValue().equals(clazz) || clazz.isAssignableFrom(entry.getValue())) {
            //noinspection unchecked
            return (T) entry.getKey();
        }
        throw new IllegalStateException("arg cannot cast to define clazz");
    }

    public Object getArg(int index, boolean npeCheck) {
        return getArg(index, Object.class, npeCheck);
    }

}
