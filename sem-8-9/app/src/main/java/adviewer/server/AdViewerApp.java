package adviewer.server;

import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.ee11.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class AdViewerApp {

    public static void main(String[] args) throws Exception {
        int port = switch (args.length) {
            case 0 -> 8080;
            case 1 -> Integer.parseInt(args[0]);
            default -> {
                System.out.println("Invalid");
                Runtime.getRuntime().exit(1);
                throw new AssertionError("inaccessible");
            }
        };

        // Create Spring context and register configuration class
        var ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(AdViewerApp.class);

        // Create DispatcherServlet
        var dispatcherServlet = new DispatcherServlet(ctx);
        var servletHolder = new ServletHolder(dispatcherServlet);

        // Jetty setup
        var server = new Server(port);
        var handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        handler.setContextPath("/");
        handler.addServlet(servletHolder, "/*");

        server.setHandler(handler);

        try {
            server.start();
            System.out.println("Server started at " + port);
            server.join();
        } finally {
            server.destroy();
        }
    }

}

