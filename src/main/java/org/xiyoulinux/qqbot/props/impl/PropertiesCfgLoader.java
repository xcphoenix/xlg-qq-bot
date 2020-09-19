package org.xiyoulinux.qqbot.props.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.xiyoulinux.qqbot.props.ResCfgLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/9/18 下午6:30
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertiesCfgLoader implements ResCfgLoader {

    private String propPath = "mirai.properties";

    @Override
    public Map<String, String> loadConfig() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propPath);
        Properties properties = new Properties();
        properties.load(inputStream);
        Map<String, String> map = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            map.put(key, properties.getProperty(key));
        }
        return map;
    }

}
