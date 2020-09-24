package org.xiyoulinux.qqbot.core.impl;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.xiyoulinux.qqbot.core.BootstrapService;
import org.xiyoulinux.qqbot.manager.EventManager;
import org.xiyoulinux.qqbot.pojo.BootstrapContext;
import org.xiyoulinux.qqbot.pojo.mirai.MiraiConstant;
import org.xiyoulinux.qqbot.props.ResCfgLoader;
import org.xiyoulinux.qqbot.props.impl.PropertiesCfgLoader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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

    public MiraiBootstrapService(@Qualifier(value = "miraiEventManager") EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void init() {
        ResCfgLoader resCfgLoader = new PropertiesCfgLoader();
        Map<String, String> resMap;
        try {
            resMap = resCfgLoader.loadConfig();
        } catch (IOException e) {
            throw new RuntimeException("load config from resource error", e);
        }

        bot = BotFactoryJvm.newBot(
                Long.parseLong(resMap.getOrDefault(MiraiConstant.QQ_PROP, "")),
                resMap.getOrDefault(MiraiConstant.PASSWD_PROP, ""),
                new BotConfiguration() {
                    {
                        redirectBotLogToDirectory(new File("./logs/mirai/bot"));
                        redirectNetworkLogToDirectory(new File("./logs/mirai/net"));
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
