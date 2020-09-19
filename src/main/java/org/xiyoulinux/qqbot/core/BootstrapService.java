package org.xiyoulinux.qqbot.core;

import org.springframework.boot.CommandLineRunner;
import org.xiyoulinux.qqbot.pojo.BootstrapContext;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/17 下午5:57
 */
public interface BootstrapService extends CommandLineRunner {

    /**
     * 初始化
     */
    default void init() {}

    /**
     * 启动入口
     */
    void startup();

    /**
     * 运行
     *
     * @param args 参数
     */
    @Override
    default void run(String... args) {
        try {
            startup();
        } catch (Exception exception) {
            throw new RuntimeException("bootstrap run error", exception);
        }
    }

    /**
     * 构建上下文
     *
     * @return 启动上下文
     */
    default BootstrapContext buildContext() {
        return new BootstrapContext(this);
    }

}
