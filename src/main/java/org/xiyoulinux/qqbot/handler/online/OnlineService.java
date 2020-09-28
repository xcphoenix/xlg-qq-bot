package org.xiyoulinux.qqbot.handler.online;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;
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

    @Value("${sign.token}")
    private String accessToken;

    private static final String ON_LINE_API = "https://sign.xiyoulinux.org/all/onUserNumber";

    private final Pattern rulePattern = Pattern.compile("^小组.*(没)?有.*人");

    private final Calendar morningTime = Calendar.getInstance();
    private final Calendar nightTime = Calendar.getInstance();
    private final OkHttpClient httpClient = new OkHttpClient().newBuilder()
            .followRedirects(true)
            .followSslRedirects(true)
            .build();

    {
        morningTime.set(HOUR_OF_DAY, 7);
        morningTime.set(MINUTE, 0);

        nightTime.set(HOUR_OF_DAY, 23);
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
            replyMsg = "糟糕...( ꒪⌓꒪) 服务出错啦";
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
        Request request = new Request.Builder().url(ON_LINE_API)
                .addHeader("X-Token", accessToken)
                .build();
        final String successFlag = "success";
        final String resultFlag = "result";
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.warn("request online error, api: {}", ON_LINE_API);
                return null;
            }
            String respJsonStr = Objects.requireNonNull(response.body()).string();
            JSONObject respJson = JSON.parseObject(respJsonStr);
            Integer num = Optional.ofNullable(respJson).filter(e -> e.getBoolean(successFlag))
                    .map(e -> e.getInteger(resultFlag)).orElse(null);
            if (num != null) {
                 return num;
            } else {
                log.warn("request online num failed, resp: {}", respJsonStr);
            }
        } catch (IOException e) {
            log.error("request online exception, api: " + ON_LINE_API, e);
        }
        return null;
    }

}
