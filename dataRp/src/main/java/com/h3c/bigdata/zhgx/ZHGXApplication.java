package com.h3c.bigdata.zhgx;

import com.h3c.bigdata.zhgx.common.utils.ToMoveTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Map;


//增加此标签 可以开启检测filter的服务
@ServletComponentScan
@EnableAsync
@EnableTransactionManagement
@EnableCaching
//增加SecurityAutoConfiguration.class 过滤 不启动 maven配置中的security的功能
@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class, SecurityAutoConfiguration.class})
@EnableWebSecurity
@EnableDiscoveryClient
@EnableScheduling //开启定时任务
public class ZHGXApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZHGXApplication.class, args);
        System.out.println("数据填报项目启动成功 ................");
    }

}
