public class Main {
    public static void main(String[] args) {

        Server server = new Server(9999, 64);

        server.addHandler(
                "GET",
                "/messages",
                (request, response) -> response.send("Hello from GET/messages")
        );

        server.addHandler(
                "POST",
                "/messages",
                (request, response) -> response.send("Hello from POST/messages")
        );

        server.listen(9999);
    }
}
