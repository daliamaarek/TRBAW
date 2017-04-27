package tweets;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the main class that is responsible for running the Tomcat server and the application using Spring Boot.
@author     Dalia Mostafa
 */
@SpringBootApplication
public class Application {

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}