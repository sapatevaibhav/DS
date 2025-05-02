import java.util.Scanner;

public class RingAlgorithm {
    public static void main(String[] args) {
        int i, j;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of processes: ");
        int numberOfProcesses = scanner.nextInt();
        Process[] processes = new Process[numberOfProcesses];
        for (i = 0; i < processes.length; i++)
            processes[i] = new Process();
        for (i = 0; i < numberOfProcesses; i++) {
            processes[i].index = i;
            System.out.println("Enter the ID of process " + (i + 1) + ": ");
            processes[i].id = scanner.nextInt();
            processes[i].state = "active";
            processes[i].hasSentMessage = false;
        }
        for (i = 0; i < numberOfProcesses - 1; i++) {
            for (j = 0; j < numberOfProcesses - i - 1; j++) {
                if (processes[j].id > processes[j + 1].id) {
                    Process temp = processes[j];
                    processes[j] = processes[j + 1];
                    processes[j + 1] = temp;
                }
            }
        }
        System.out.println("Processes in sorted order:");
        for (i = 0; i < numberOfProcesses; i++) {
            System.out.println("[" + processes[i].index + "] " + processes[i].id);
        }
        int initiatorIndex;
        while (true) {
            System.out.println("\n1. Initiate Election\n2. Quit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter the index of the process initiating the election: ");
                    initiatorIndex = scanner.nextInt();
                    for (i = 0; i < numberOfProcesses; i++) {
                        processes[i].hasSentMessage = false;
                    }
                    initiateElection(processes, initiatorIndex, numberOfProcesses);
                    break;
                case 2:
                    System.out.println("Program terminated.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid response.");
            }
        }
    }

    public static void initiateElection(Process[] processes, int initiatorIndex, int numberOfProcesses) {
        System.out.println("Election initiated by Process " + processes[initiatorIndex].id);
        int[] electionMessage = new int[1];
        electionMessage[0] = processes[initiatorIndex].id;
        int currentIndex = initiatorIndex;
        do {
            currentIndex = (currentIndex + 1) % numberOfProcesses;
            if (processes[currentIndex].state.equals("active")) {
                System.out.println("Process " + processes[currentIndex].id + " received election message");
                if (processes[currentIndex].id > findMax(electionMessage)) {
                    int[] newMessage = new int[electionMessage.length + 1];
                    System.arraycopy(electionMessage, 0, newMessage, 0, electionMessage.length);
                    newMessage[electionMessage.length] = processes[currentIndex].id;
                    electionMessage = newMessage;
                    System.out.println("Process " + processes[currentIndex].id + " added its ID to election message");
                }
                System.out.println("Process " + processes[currentIndex].id + " forwards election message");
            }
        } while (currentIndex != initiatorIndex);
        int coordinatorID = findMax(electionMessage);
        int coordinatorIndex = -1;
        for (int i = 0; i < processes.length; i++) {
            if (processes[i].id == coordinatorID) {
                coordinatorIndex = i;
                break;
            }
        }
        System.out.println("\nElection completed!");
        System.out.println("Process " + coordinatorID + " becomes the coordinator.");
        currentIndex = coordinatorIndex;
        do {
            System.out.println("Process " + processes[currentIndex].id +
                    (processes[currentIndex].id == coordinatorID ? " (coordinator)" : "") +
                    " is informed about the new coordinator");
            currentIndex = (currentIndex + 1) % numberOfProcesses;
        } while (currentIndex != coordinatorIndex);
    }

    private static int findMax(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
}

class Process {
    public int index;
    public int id;
    public boolean hasSentMessage;
    public String state;
}
