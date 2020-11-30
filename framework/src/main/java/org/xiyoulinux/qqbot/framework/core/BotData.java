package org.xiyoulinux.qqbot.framework.core;

import lombok.Getter;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/29 下午5:56
 */
@Getter
public abstract class BotData<T> {

    public BotData(T implBot) {
        this.implBot = implBot;
    }

    protected T implBot;

    protected long qq;

    protected long[] friends;

    protected long[] groups;

}
