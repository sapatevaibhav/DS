// javac *.java
// java WebService

// java Client
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WebService {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/add", (HttpExchange exchange) -> {
            String query = exchange.getRequestURI().getQuery();
            String[] params = query.split("&");
            int a = Integer.parseInt(params[0].split("=")[1]);
            int b = Integer.parseInt(params[1].split("=")[1]);
            int sum = a + b;

            String response = "Sum is: " + sum;
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.start();
        System.out.println("Web service running on http://localhost:8080/add?a=1&b=2");
    }
}
