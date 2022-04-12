package cn.wskweb.reggie_take_out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j          // 日志
@ServletComponentScan       // 开启基于注解方式的Servlet组件扫描支持
@SpringBootApplication      // 主启动类
@EnableTransactionManagement    // 开启事务支持
public class ReggieTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieTakeOutApplication.class, args);
        log.info("项目启动成功");
    }

}
