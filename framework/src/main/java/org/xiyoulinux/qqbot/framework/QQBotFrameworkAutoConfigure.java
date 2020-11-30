package org.xiyoulinux.qqbot.framework;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.xiyoulinux.qqbot.framework.config.QQBotConfig;

/**
 * @author xuanc
 */
@Configuration
@Import({QQBotConfig.class})
public class QQBotFrameworkAutoConfigure {

}
