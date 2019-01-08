package cn.jgayb.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RestController
@RequestMapping("/test")
public class WebsocketDemoApplication {

    @Autowired
    private WebPushServiceImpl webPushService;

    public static void main(String[] args) {
        SpringApplication.run(WebsocketDemoApplication.class, args);
    }

    @GetMapping("/send")
    public Mono<String> sendMessage() {
        webPushService.sendMessage("token=12345", "push test");
        return Mono.just("success");
    }
}
