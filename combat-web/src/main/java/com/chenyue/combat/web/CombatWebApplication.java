package com.chenyue.combat.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@MapperScan("com.chenyue.combat.server.mapper")
@SpringBootApplication(scanBasePackages = {"com.chenyue.combat"})
public class CombatWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CombatWebApplication.class, args);
    }

}
