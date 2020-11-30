package org.xiyoulinux.qqbot.framework.core;

import lombok.Getter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/29 下午5:58
 */
@Getter
public class MiraiBotData extends BotData<Bot> {

    public MiraiBotData(Bot implBot) {
        super(implBot);
        this.qq = implBot.getId();
        this.friends = implBot.getFriends().stream().mapToLong(Friend::getId).toArray();
        this.groups = implBot.getGroups().stream().mapToLong(Group::getId).toArray();
    }

}
