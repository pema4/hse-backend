package todo.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"todo.server", "todo.domain"})
public class SpringTodoApp {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(SpringTodoApp.class);
        context.start();
    }

}
