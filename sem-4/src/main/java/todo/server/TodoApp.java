package todo.server;

import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DebugHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.List;
import java.util.concurrent.Executors;

public class TodoApp {
    void main() throws Exception {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setVirtualThreadsExecutor(Executors.newVirtualThreadPerTaskExecutor());

        Server server = new Server(threadPool);

        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(8080);
        server.addConnector(serverConnector);

        var handlerSequence = new Handler.Sequence();
        handlerSequence.setHandlers(handlers());

        var debugHandler = new DebugHandler(handlerSequence);
        debugHandler.setOutputStream(System.out);
        server.setHandler(debugHandler);

        CustomRequestLog requestLog = new CustomRequestLog(System.out::println, CustomRequestLog.EXTENDED_NCSA_FORMAT);
        server.setRequestLog(requestLog);
        server.start();
        IO.println("Jetty started");
    }

    private List<Handler> handlers() throws Exception {
        var servletContext = new ServletContextHandler("/");
//        servletContext.addServlet(TodoServlet.class, "/todo");
        return List.of(new HealthHandler(), servletContext);
    }

}
