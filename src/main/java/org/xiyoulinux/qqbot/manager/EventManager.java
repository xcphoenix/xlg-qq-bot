package org.xiyoulinux.qqbot.manager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.xiyoulinux.qqbot.pojo.BootstrapContext;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/17 下午6:02
 */
public abstract class EventManager implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 注册事件 {@link org.xiyoulinux.qqbot.handle.EventHandle}
     *
     * @param context 启动上下文
     */
    public abstract void registerEventHandle(BootstrapContext context);

}
