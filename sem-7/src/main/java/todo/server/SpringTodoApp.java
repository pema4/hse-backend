package todo.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringTodoApp {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(SpringTodoApp.class);
        context.start();
    }

}
