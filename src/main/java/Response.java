import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Response {
    private final static String DEFAULT_CONTENT_TYPE = "text/plain";

    private final BufferedOutputStream out;

    public Response(BufferedOutputStream out) {
        this.out = out;
    }

    public void send(String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        send(bytes, DEFAULT_CONTENT_TYPE);
    }

    public void send(byte[] body, String contentType) throws IOException {
        out.write((
            "HTTP/1.1 200 OK \r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + body.length + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n"
        ).getBytes());
        out.write(body);
        out.flush();
    }
}
