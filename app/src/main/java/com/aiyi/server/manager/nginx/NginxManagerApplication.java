package com.aiyi.server.manager.nginx;

import com.aiyi.server.manager.nginx.utils.PropsUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NginxManagerApplication {

    public static void main(String[] args) {
        PropsUtils.setSysPropByArgs(args);
        SpringApplication.run(NginxManagerApplication.class, args);
    }
}
