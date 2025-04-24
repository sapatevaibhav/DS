import java.io.*;
import java.net.*;

public class TokenRing {
    
    static final int PORT = 12345;
    static final String HOST = "localhost";
    static final int NUM_NODES = 3; // number of nodes in the ring
    
    static int token = 0; // the current holder of the token
    static int[] request = new int[NUM_NODES]; // request message flags
    
    static class Process implements Runnable {
        
        private int id;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        
        public Process(int id) {
            this.id = id;
        }
        
        public void run() {
            try {
                // connect to the next process in the ring
                socket = new Socket(HOST, PORT + (id + 1) % NUM_NODES);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                // start listening for messages
                new Thread(new MessageListener()).start();
                
                // wait for a moment to ensure all processes are started
                Thread.sleep(1000);
                
                if (id == 0) {
                    // start the token with process 0
                    token = 0;
                    request[0] = 0;
                }
                
                // start requesting and releasing the shared resource
                while (true) {
                    Thread.sleep((int) (Math.random() * 5000));
                    requestCS();
                    Thread.sleep(1000);
                    releaseCS();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null) socket.close();
                    if (in != null) in.close();
                    if (out != null) out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private void requestCS() {
            // send a request message to the next process in the ring
            request[id] = 1;
            out.println("request");
        }
        
        private void releaseCS() {
            if (id == token) {
                // release the token and pass it to the next process in the ring
                request[(id + 1) % NUM_NODES] = 0;
                token = (id + 1) % NUM_NODES;
                out.println("token");
            } else {
                out.println("idle");
            }
        }
        
        private class MessageListener implements Runnable {
            public void run() {
                try {
                    while (true) {
                        String message = in.readLine();
                        if (message.equals("request")) {
                            if (token == id) {
                                // handle the request and pass the token
                                request[(id + 1) % NUM_NODES] = 1;
                                token = (id + 1) % NUM_NODES;
                                out.println("token");
                            } else {
                                // forward the request
                                out.println("request");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < NUM_NODES; i++) {
            new Thread(new Process(i)).start();
        }
    }
}
