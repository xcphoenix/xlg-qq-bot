package org.xiyoulinux.qqbot.core;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/17 下午5:57
 */
public abstract class BootstrapService implements CommandLineRunner {

    protected final int FAILED_MAX_TIME = 3;

    /**
     * 初始化
     */
    protected void init() {}

    protected void destroy(Throwable throwable) {}

    /**
     * 启动入口
     */
    protected abstract void startup();

    /**
     * 运行
     *
     * @param args 参数
     */
    @Override
    public void run(String... args) {
        int failedTimes = 0;
        do {
            try {
                init();
                startup();
                destroy(null);
            } catch (Exception exception) {
                new RuntimeException("bootstrap run error, failed " + (++failedTimes) + "times", exception)
                        .printStackTrace();
                destroy(exception);
            }
        } while (failedTimes > 0 && failedTimes < FAILED_MAX_TIME);
    }

    /**
     * 获取上下文
     *
     * @return 启动上下文
     */
    @NotNull
    public BootstrapContext getContext() {
        return new BootstrapContext(this);
    }

}
