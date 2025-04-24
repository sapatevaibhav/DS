import java.util.Scanner;
public class BullyAlgorithm {
    static boolean[] state = new boolean[5];
    static int coordinator = 5;
    public static void up(int up) {
        if (state[up - 1]) {
            System.out.println("Process " + up + " is already up.");
        } else {
            state[up - 1] = true;
            System.out.println("Process " + up + " is up.");
            if (up > coordinator) {
                System.out.println("Process " + up + " initiates an election.");
                election(up);
            }
        }
    }
    public static void down(int down) {
        if (!state[down - 1]) {
            System.out.println("Process " + down + " is already down.");
        } else {
            state[down - 1] = false;
            System.out.println("Process " + down + " is down.");
            if (down == coordinator) {
                System.out.println("Coordinator (Process " + down + ") is down.");
                for (int i = 5; i >= 1; i--) {
                    if (state[i - 1] && i != down) {
                        System.out.println("Process " + i + " detects coordinator is down.");
                        election(i);
                        break;
                    }
                }
            }
        }
    }
    public static void mess(int mess) {
        if (!state[mess - 1]) {
            System.out.println("Process " + mess + " is down.");
        } else {
            if (mess == coordinator) {
                System.out.println("Coordinator (Process " + coordinator + ") received the message: OK");
            } else {
                System.out.println("Process " + mess + " sends a message to coordinator.");
                if (!state[coordinator - 1]) {
                    System.out.println("Coordinator is down! Process " + mess + " initiates election.");
                    election(mess);
                } else {
                    System.out.println("Coordinator (Process " + coordinator + ") responds: OK");
                }
            }
        }
    }
    public static void election(int initiator) {
        System.out.println("Election initiated by Process " + initiator);
        boolean higherProcessResponded = false;
        for (int i = initiator + 1; i <= 5; i++) {
            if (state[i - 1]) {
                System.out.println("Process " + initiator + " sends election message to Process " + i);
                System.out.println("Process " + i + " responds with OK to Process " + initiator);
                higherProcessResponded = true;
                System.out.println("Process " + i + " starts an election.");
                boolean anyHigherProcess = false;
                for (int j = i + 1; j <= 5; j++) {
                    if (state[j - 1]) {
                        anyHigherProcess = true;
                        System.out.println("Process " + i + " sends election message to Process " + j);
                        System.out.println("Process " + j + " responds with OK to Process " + i);
                    }
                }
                if (!anyHigherProcess) {
                    coordinator = i;
                    announceCoordinator(i);
                    return;
                }
            }
        }
        if (!higherProcessResponded) {
            coordinator = initiator;
            announceCoordinator(initiator);
        }
    }
    public static void announceCoordinator(int newCoordinator) {
        System.out.println("Process " + newCoordinator + " becomes the new coordinator.");
        System.out.println("Process " + newCoordinator + " sends coordinator message to all processes:");
        for (int i = 1; i < newCoordinator; i++) {
            if (state[i - 1]) {
                System.out.println("- Process " + i + " receives coordinator message from Process " + newCoordinator);
            }
        }
    }
    public static void main(String[] args) {
        int choice;
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 5; ++i) {
            state[i] = true;
        }
        System.out.println("5 active processes are:");
        System.out.println("Process up  = p1 p2 p3 p4 p5");
        System.out.println("Process 5 is coordinator");
        do {
            System.out.println(".........");
            System.out.println("1. Up a process.");
            System.out.println("2. Down a process.");
            System.out.println("3. Send a message.");
            System.out.println("4. Exit.");
            choice = sc.nextInt();
            switch (choice) {
                case 1: {
                    System.out.println("Bring process up:");
                    int up = sc.nextInt();
                    if (up > 5) {
                        System.out.println("Invalid process number.");
                        break;
                    }
                    up(up);
                    break;
                }
                case 2: {
                    System.out.println("Bring down any process:");
                    int down = sc.nextInt();
                    if (down > 5) {
                        System.out.println("Invalid process number.");
                        break;
                    }
                    down(down);
                    break;
                }
                case 3: {
                    System.out.println("Which process will send message:");
                    int mess = sc.nextInt();
                    if (mess > 5) {
                        System.out.println("Invalid process number.");
                        break;
                    }
                    mess(mess);
                    break;
                }
            }
            sc.close();
        } while (choice != 4);
    }
}
