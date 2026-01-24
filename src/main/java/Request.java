import java.io.InputStream;
import java.util.Map;

public class Request {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final InputStream body;

    public Request(String method, String path, Map<String, String> headers, InputStream body) {
        this.body = body;
        this.method = method;
        this.path = path;
        this.headers = headers;
    }

    public InputStream getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
