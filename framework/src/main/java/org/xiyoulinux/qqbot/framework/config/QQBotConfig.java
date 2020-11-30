package org.xiyoulinux.qqbot.framework.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xiyoulinux.qqbot.framework.core.BootstrapService;
import org.xiyoulinux.qqbot.framework.core.impl.MiraiBootstrapService;
import org.xiyoulinux.qqbot.framework.handle.EventManager;
import org.xiyoulinux.qqbot.framework.handle.mirai.MiraiMessageEventManager;
import org.xiyoulinux.qqbot.framework.utils.SpringUtils;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/29 下午8:51
 */
@Configuration
public class QQBotConfig {

    @Bean
    public SpringUtils getSpringUtils() {
        return new SpringUtils();
    }

    @Bean
    @ConditionalOnMissingBean({BootstrapService.class})
    public BootstrapService getBootstrapService() {
        return new MiraiBootstrapService();
    }

    @Bean
    @ConditionalOnMissingBean({MiraiMessageEventManager.class})
    public EventManager getMessageEventManager() {
        return new MiraiMessageEventManager();
    }

}
