import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    //private final int port;
    private final ExecutorService threadPool;
    private final Map<String, Map<String, Handler>> handlers;

    public Server() {
        //this.port = 9999;
        this.threadPool = Executors.newFixedThreadPool(64);
        this.handlers = new ConcurrentHashMap<>();
    }

    public Server(int port, int threadPool) {
        //this.port = port;
        this.threadPool = Executors.newFixedThreadPool(threadPool);
        this.handlers = new ConcurrentHashMap<>();
    }

    public void addHandler(String method, String path, Handler handler) {
        handlers.computeIfAbsent(method, k -> new ConcurrentHashMap<>())
                .put(path, handler);
    }

    public void listen(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                threadPool.submit(() -> handleConnection(socket));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void handleConnection(Socket socket) {
        try (
                socket;
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            final var requestLine = in.readLine();
            if (requestLine == null) {
                return;
            }

            final var parts = requestLine.split(" ");
            if (parts.length != 3) {
                return;
            }

            final var method = parts[0];
            final var fullPath = parts[1];

            Map<String, String> headers = new HashMap<>();
            String line;

            while (!(line = in.readLine()).isEmpty()) {
                String[] header = line.split(": ", 2);
                headers.put(header[0], header[1]);
            }

            Request request = new Request(method, fullPath, headers, socket.getInputStream());

            //логирование для проверки
            System.out.println(request.getQueryParams());
            //System.out.println(request.getQueryParam("last"));

            Handler handler = handlers
                    .getOrDefault(method, Map.of())
                    .get(request.getPath()); //Для хендлера получаем не полный путь а "урезанный" в методе getPath класса Request

            if (handler == null) {
                out.write((
                        "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
            }

            Response response = new Response(out);
            handler.handle(request, response);

            out.flush();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}