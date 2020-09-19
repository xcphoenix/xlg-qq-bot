package org.xiyoulinux.qqbot.handle.mirai.impl.online;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/19 上午11:34
 */
@Slf4j
@Component
public class OnlineService {

    private static final String ON_LINE_API = ";";

    private final Pattern rulePattern = Pattern.compile("^小组.*[有|没有].*人");

    private final Calendar morningTime = Calendar.getInstance();
    private final Calendar nightTime = Calendar.getInstance();
    private final OkHttpClient httpClient = new OkHttpClient().newBuilder()
            .followRedirects(true)
            .followSslRedirects(true)
            .build();

    {
        morningTime.set(HOUR_OF_DAY, 7);
        morningTime.set(MINUTE, 0);

        nightTime.set(HOUR_OF_DAY, 24);
        nightTime.set(MINUTE, 0);
    }

    public void onlineHandle(String msg, Consumer<String> action) {
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        if (!rulePattern.matcher(msg).find()) {
            return;
        }
        Integer num = getOnlineNum();
        String replyMsg;
        if (num == null) {
            replyMsg = "服务出错啦";
        } else if (num <= 0) {
            Calendar now = Calendar.getInstance();
            if (now.getTime().after(nightTime.getTime())) {
                replyMsg = "已经很晚了~小组已经没有人了";
            } else if (now.getTime().before(morningTime.getTime())) {
                replyMsg = "太早了呀，小组还没有人~";
            } else {
                replyMsg = "小组居然没有人欸~";
            }
        } else {
            replyMsg = "小组现在有" + num + "个人";
        }
        action.accept(replyMsg);
    }

    private Integer getOnlineNum() {
        return 1;
        // Request request = new Request.Builder().url(ON_LINE_API).build();
        // try (Response response = httpClient.newCall(request).execute()) {
        //     if (response.isSuccessful()) {
        //         // TODO 解析
        //         return 1;
        //     } else {
        //         log.warn("request online error, api: {}", ON_LINE_API);
        //     }
        // } catch (IOException e) {
        //     log.error("request online exception, api: " + ON_LINE_API, e);
        // }
        // return null;
    }

}
