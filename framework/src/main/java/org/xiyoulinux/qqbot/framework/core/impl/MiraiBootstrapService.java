package org.xiyoulinux.qqbot.framework.core.impl;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.xiyoulinux.qqbot.framework.core.BootstrapContext;
import org.xiyoulinux.qqbot.framework.core.BootstrapService;
import org.xiyoulinux.qqbot.framework.core.MiraiBotData;
import org.xiyoulinux.qqbot.framework.handle.EventManager;
import org.xiyoulinux.qqbot.framework.utils.SpringUtils;

import java.io.File;
import java.util.Map;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/18 下午3:06
 */
@Slf4j
public class MiraiBootstrapService extends BootstrapService {

    private Bot bot;
    private volatile boolean init = false;
    private final BootstrapContext context = new BootstrapContext(this);

    @Value("${mirai.qq}")
    private Long qq;

    @Value("${mirai.password}")
    private String passwd;

    @Value("${mirai.log.redirect:false}")
    private boolean redirectLog;

    @Override
    synchronized protected void init() {
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
        context.putArg(new MiraiBotData(bot));
        init = true;
    }

    @Override
    protected void destroy(Throwable throwable) {
        log.warn("service destroy...");
        init = false;
        if (bot != null && bot.isOnline()) {
            bot.close(throwable);
            bot = null;
        }
        log.warn("service destroyed");
    }

    @Override
    public void startup() {
        log.info("Mirai Service Start...");
        Map<String, EventManager> eventManagerMap = SpringUtils.getBeansOfType(EventManager.class);
        eventManagerMap.forEach((beanName, beanInstance) -> {
            log.info("register event manger [name = {}, type = {}]", beanName, beanInstance.getType().name());
            beanInstance.registerEventHandle(getContext());
        });
        log.info("Mirai Bot Join...");
        bot.join();
        log.info("Mirai Bot Join OK");
    }

    @Override
    public BootstrapContext getContext() {
        Assert.state(init, "service have not init");
        return context;
    }

}
