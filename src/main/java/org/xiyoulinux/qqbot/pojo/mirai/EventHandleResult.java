package org.xiyoulinux.qqbot.pojo.mirai;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.mamoe.mirai.event.ListeningStatus;

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

    /**
     * 持续监听还是停止 仅当instantResult为true是有效
     */
    private ListeningStatus status = ListeningStatus.LISTENING;

    public static boolean isInstantResult(EventHandleResult result) {
        if (result == null) {
            return false;
        }
        return result.isInstantResult() && result.getStatus() != null;
    }

}
