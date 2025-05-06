import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        String query = "http://localhost:8080/add?a=100&b=15";
        URL url = new URL(query);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        Scanner scanner = new Scanner(conn.getInputStream());
        while (scanner.hasNext()) {
            System.out.println(scanner.nextLine());
        }
        scanner.close();
        conn.disconnect();
    }
}
