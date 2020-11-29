package org.xiyoulinux.qqbot.handle.mirai;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Optional;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/17 下午6:10
 */
@Getter
@Setter
@Accessors(chain = true)
public class EventHandleResult {

    public static final EventHandleResult DEFAULT_RESULT = new EventHandleResult();

    /**
     * 是否立即结束本次时间
     */
    private boolean instantResult = false;

    public static boolean checkInstantResult(EventHandleResult result) {
        return Optional.ofNullable(result).map(EventHandleResult::isInstantResult).orElse(false);
    }

}
