package sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 *  Здесь важно понять порядок, в котором все происходит.
 *  Чтобы увидеть работу Spring'а, можно повысить детализацию логов: добавить в файл resources/application.properties строку
 *  <code>logging.level.root = debug (или trace)</code>
 *
 *  1. Сначала мы все так же попадаем в функцию main и стартуем Spring-приложение
 *
 *  2. Спринг подсасывает в себя единственный класс, который мы ему передали, Application.class
 *
 *  3. По аннотации @SpringBootConfiguration(которая по сути аналогична @Configuration) он понимает, что это класс конфигурации
 *
 *  4. Далее он находит аннотацию @EnableAutoConfiguration и собирает стандартные автоконфигурационные классы. Один из них на более поздней
 *  стадии запуска, найдет, что мы подключили в зависимости сервер Jetty, и запустит его со стандартным DispatcherServlet.
 *  Этот сервлет вы можете увидет в стектрейсе, если сделаете точку останова в методе UserController.
 *
 *  5. Аннотация @EnableAutoScan приведет к тому, что Spring просканирует пакет sample (а так же все вложенные пакеты) и подсосет еще и классы,
 *  которые найдет там. Т.е классы, которые помечены аннотацией @Component. Для @RestController'а аннотация @Component лежит в
 *  '@RestController->@Controller
 *
 *  6. В @RestController'е Spring находит методы помеченные @RequestMapping, запоминает их, чтобы DispatcherServlet, потом, смог произвести вызов
 *  на соответствующий http-запрос.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
