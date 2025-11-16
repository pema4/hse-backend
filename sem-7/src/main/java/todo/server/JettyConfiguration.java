package todo.server;

import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DebugHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.PropertySources;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

@Component
public class JettyConfiguration {
    @Bean
    SmartLifecycle serverLifecycle(Server server) {
        return new SmartLifecycle() {
            @Override
            public void start() {
                try {
                    server.start();
                    IO.println("Server started");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void stop() {
                try {
                    server.stop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean isRunning() {
                return server.isRunning();
            }
        };
    }

    @Bean
    Server server(Handler serverHandler) {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setVirtualThreadsExecutor(Executors.newVirtualThreadPerTaskExecutor());

        Server server = new Server(threadPool);

        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(8080);
        server.addConnector(serverConnector);

        server.setHandler(serverHandler);

        CustomRequestLog requestLog = new CustomRequestLog(System.out::println, CustomRequestLog.EXTENDED_NCSA_FORMAT);
        server.setRequestLog(requestLog);

        return server;
    }

    @Bean
    @Primary
    Handler serverHandler(HealthHandler healthHandler, TodoHandler todoHandler) {
        var handlerSequence = new Handler.Sequence();
        handlerSequence.setHandlers(List.of(healthHandler, todoHandler));

        var debugHandler = new DebugHandler(handlerSequence);
        debugHandler.setOutputStream(System.out);
        return debugHandler;
    }
}
