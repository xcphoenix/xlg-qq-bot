package org.xiyoulinux.qqbot.core.impl;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xiyoulinux.qqbot.core.BootstrapService;
import org.xiyoulinux.qqbot.manager.EventManager;
import org.xiyoulinux.qqbot.pojo.BootstrapContext;

import java.io.File;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/18 下午3:06
 */
@Component
@Slf4j
public class MiraiBootstrapService implements BootstrapService {

    private final EventManager eventManager;
    private Bot bot;

    @Value("${mirai.qq}")
    private Long qq;

    @Value("${mirai.password}")
    private String passwd;

    @Value("${mirai.log.redirect:false}")
    private boolean redirectLog;

    public MiraiBootstrapService(@Qualifier(value = "miraiEventManager") EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void init() {
        bot = BotFactoryJvm.newBot(
                qq, passwd,
                new BotConfiguration() {
                    {
                        if (redirectLog) {
                            redirectBotLogToDirectory(new File("./logs/mirai/bot"));
                            redirectNetworkLogToDirectory(new File("./logs/mirai/net"));
                        }
                        fileBasedDeviceInfo("deviceInfo.json");
                    }
                });
        bot.login();
    }

    @Override
    public void startup() {
        log.info("Mirai Service Start...");
        this.init();
        eventManager.registerEventHandle(buildContext());
        bot.join();
    }

    @Override
    public BootstrapContext buildContext() {
        BootstrapContext context = new BootstrapContext(this);
        context.setArgs(bot);
        return context;
    }

}
