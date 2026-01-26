import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class Request {
    private final String method;
    private final String fullPath;
    private final Map<String, String> headers;
    private final InputStream body;

    public Request(String method, String fullPath, Map<String, String> headers, InputStream body) {
        this.body = body;
        this.method = method;
        this.fullPath = fullPath;
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

    //Возвращаем только пкть без параметров
    public String getPath() {
        return URI.create(fullPath).getPath();
    }

    public List<NameValuePair> getQueryParams() {
        return URLEncodedUtils.parse(URI.create(fullPath), Charset.defaultCharset());
    }

    public List<NameValuePair> getQueryParam(String name) {
        return getQueryParams().;
        return URLEncodedUtils.parse(URI.create(fullPath), Charset.defaultCharset());
    }
}
