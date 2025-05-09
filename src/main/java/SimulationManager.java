import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SimulationManager extends Thread {
    private final int timeLimit;
    private final int numberOfClients;
    private final int numberOfServers;
    private final int minArrival;
    private final int maxArrival;
    private final int minService;
    private final int maxService;

    private final List<Client> generatedClients = new ArrayList<>();
    private final Scheduler scheduler;
    private final FileWriter logWriter;
    private final JTextArea logArea;

    public SimulationManager(int timeLimit, int numberOfClients, int numberOfServers,
                             int minArrival, int maxArrival, int minService, int maxService,
                             JTextArea logArea) throws IOException {
        this.timeLimit = timeLimit;
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        this.minArrival = minArrival;
        this.maxArrival = maxArrival;
        this.minService = minService;
        this.maxService = maxService;
        this.logArea = logArea;

        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < numberOfServers; i++) {
            Server server = new Server();
            servers.add(server);
            server.start();
        }
        this.scheduler = new Scheduler(servers);
        this.logWriter = new FileWriter("log.txt");
        generateClients();
    }

    private void generateClients() {
        Random rand = new Random();
        for (int i = 1; i <= numberOfClients; i++) {
            int arrival = minArrival + rand.nextInt(maxArrival - minArrival + 1);
            int service = minService + rand.nextInt(maxService - minService + 1);
            generatedClients.add(new Client(i, arrival, service));
        }
        generatedClients.sort(Comparator.comparingInt(Client::getArrivalTime));
    }

    private void appendToLogArea(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    public void run() {
        int currentTime = 0;
        try {
            while (currentTime <= timeLimit || !generatedClients.isEmpty() || !allQueuesEmpty()){

                logWriter.write("Time " + currentTime + "\n");
                appendToLogArea("Time " + currentTime);

                logWriter.write("Waiting clients: ");
                appendToLogArea("Waiting clients: ");
                List<Client> toRemove = new ArrayList<>();
                for (Client c : generatedClients) {
                    if (c.getArrivalTime() <= currentTime) {
                        scheduler.dispatchClient(c);
                        toRemove.add(c);
                    } else {
                        logWriter.write(c + "; ");
                        appendToLogArea(c + "; ");
                    }
                }
                generatedClients.removeAll(toRemove);
                logWriter.write("\n");
                appendToLogArea("");

                int serverIdx = 1;
                for (Server server : scheduler.getServers()) {
                    String queueContents = server.getQueue().isEmpty() ? "gol" : server.getQueue().toString();
                    String queueStatus = "Queue " + serverIdx++ + ": " + queueContents;

                    logWriter.write(queueStatus + "\n");
                    appendToLogArea(queueStatus);
                }

                logWriter.write("\n");
                appendToLogArea("");
                currentTime++;
                Thread.sleep(1000);
            }

            logWriter.write("Simulation ended.\n");
            appendToLogArea("Simulation ended.");
            logWriter.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean allQueuesEmpty() {
        for (Server server : scheduler.getServers()) {
            if (!server.isEmpty()) return false;
        }
        return true;
    }
}
