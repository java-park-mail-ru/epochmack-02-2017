package techpark.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import techpark.Application;

/**
 * Created by Варя on 29.03.2017.
 */
@SpringBootApplication
@Import(Application.class)
public class ApplicationTest {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
