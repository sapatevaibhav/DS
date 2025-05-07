// javac Berkeley.java
// java Berkeley

import java.io.*;
import java.net.*;
import java.util.*;

public class Berkeley {

    private static final int PORT = 9876;
    private static final int NUM_CLIENTS = 3;
    private static final List<Long> timeDiffs = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(PORT);
        Thread timeServerThread = new Thread(() -> {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                    Date clientTime = (Date) in.readObject();
                    out.writeObject(new Date());

                    long timeDiff = new Date().getTime() - clientTime.getTime();
                    timeDiffs.add(timeDiff);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        timeServerThread.start();

        for (int i = 0; i < NUM_CLIENTS; i++) {
            Thread clientThread = new Thread(new TimeClient(i));
            clientThread.start();
        }
        Thread.sleep(5000);

        long sumTimeDiff = 0;
        for (Long timeDiff : timeDiffs) {
            sumTimeDiff += timeDiff;
        }
        long avgTimeDiff = sumTimeDiff / timeDiffs.size();
        System.out.println("Average time difference: " + avgTimeDiff + " ms");

        adjustClock(avgTimeDiff);
    }

    private static void adjustClock(long avgTimeDiff) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MILLISECOND, (int) avgTimeDiff);
        System.out.println("Adjusted time: " + calendar.getTime());
    }

    static class TimeClient implements Runnable {
        private final int clientId;

        public TimeClient(int clientId) {
            this.clientId = clientId;
        }

        public void run() {
            while (true) {
                try (Socket socket = new Socket("localhost", PORT);
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                    Date myTime = new Date();
                    out.writeObject(myTime);

                    Date serverTime = (Date) in.readObject();
                    long timeDiff = serverTime.getTime() - new Date().getTime();
                    timeDiffs.add(timeDiff);

                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
