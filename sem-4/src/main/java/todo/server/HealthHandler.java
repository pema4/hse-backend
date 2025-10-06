package todo.server;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.Callback;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HealthHandler extends Handler.Abstract.NonBlocking {
    @Override
    public boolean handle(Request request, Response response, Callback callback) {
        HttpMethod httpMethod = HttpMethod.fromString(request.getMethod());
        HttpURI uri = request.getHttpURI();
        if (httpMethod != HttpMethod.GET || !uri.getPath().equals("/todo")) {
            return false;
        }

        response.setStatus(HttpStatus.OK_200);
        response.getHeaders().add(MimeTypes.Type.TEXT_PLAIN.getContentTypeField(UTF_8));
        response.write(true, BufferUtil.toBuffer("Healthy", UTF_8).compact(), callback);
        return true;
    }
}
