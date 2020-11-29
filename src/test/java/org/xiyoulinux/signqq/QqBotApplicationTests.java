package org.xiyoulinux.signqq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.xiyoulinux.qqbot.core.BootstrapContext;

import java.util.HashMap;

@SpringBootTest
@SpringBootConfiguration
class QqBotApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testBootstrapContext() {
        BootstrapContext context = new BootstrapContext(null);
        context.putArg("Hello World");
        context.putArg(12L);
        context.putArg(new HashMap<>());
        System.out.println(context.getArg(0, false));
        System.out.println(context.getArg(1, false));
        System.out.println(context.getArg(2, false));
        System.out.println(context.getArg(3, false));
    }

}
