package org.xiyoulinux.qqbot.pojo;

import lombok.Getter;
import lombok.Setter;
import org.xiyoulinux.qqbot.core.BootstrapService;

/**
 * 事件上下文
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/9/18 下午3:12
 */
@Getter
@Setter
public class BootstrapContext {

    /**
     * 启动服务
     */
    private BootstrapService bootstrapService;

    /**
     * 上下文附加参数
     */
    private Object[] args;

    public BootstrapContext(BootstrapService bootstrapService) {
        this.bootstrapService = bootstrapService;
    }

    public void setArgs(Object... args) {
        this.args = args;
    }

}
