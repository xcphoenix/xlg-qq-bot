package org.xiyoulinux.qqbot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xiyoulinux.qqbot.core.BootstrapService;
import org.xiyoulinux.qqbot.core.impl.MiraiBootstrapService;
import org.xiyoulinux.qqbot.handle.EventManager;
import org.xiyoulinux.qqbot.handle.mirai.MiraiMessageEventManager;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/29 下午8:51
 */
@Configuration
public class QQBotConfig {

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
