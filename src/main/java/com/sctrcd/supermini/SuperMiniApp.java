package com.sctrcd.supermini;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Starts the application and logs its active profiles and bean names, which shows at a glance what a Cloud Foundry
 * push has activated.
 *
 * @author Stephen Masters
 */
@SpringBootApplication
public class SuperMiniApp {

    private static final Logger log = LoggerFactory.getLogger(SuperMiniApp.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SuperMiniApp.class, args);

        String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
        StringBuilder sb = new StringBuilder("\n\n    Active profiles:\n");
        if (activeProfiles.length == 0) {
            sb.append("        No active profiles.\n");
        } else {
            for (String profile : activeProfiles) {
                sb.append("        ").append(profile).append('\n');
            }
        }

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);

        sb.append("\n    Application beans:\n");
        for (String beanName : beanNames) {
            sb.append("        ").append(beanName).append('\n');
        }
        log.info(sb.toString());
    }
}
