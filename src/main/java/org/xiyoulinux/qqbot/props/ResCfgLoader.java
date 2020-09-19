package org.xiyoulinux.qqbot.props;

import java.io.IOException;
import java.util.Map;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/18 下午6:27
 */
public interface ResCfgLoader {

    /**
     * 加载配置
     *
     * @return 配置信息
     */
    Map<String, String> loadConfig() throws IOException;

}
