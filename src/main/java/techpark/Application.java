package techpark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import techpark.resources.Generator;
import techpark.websocket.GameSocketHandler;

@SpringBootApplication
@ComponentScan
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Object[]{WebSocketConfig.class, Application.class}, args);
    }

    @Bean
    public WebSocketHandler gameWebSocketHandler() {
        return new PerConnectionWebSocketHandler(GameSocketHandler.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

