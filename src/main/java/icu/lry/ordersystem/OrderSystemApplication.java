package icu.lry.ordersystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class OrderSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderSystemApplication.class, args);
        log.info("订单系统启动成功");
    }

}
