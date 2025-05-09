
import java.util.List;

public class Scheduler {
    private final List<Server> servers;

    public Scheduler(List<Server> servers) {
        this.servers = servers;
    }

    public void dispatchClient(Client client) {
        Server bestServer = servers.get(0);
        for (Server server : servers) {
            if (server.getWaitingPeriod() < bestServer.getWaitingPeriod()) {
                bestServer = server;
            }
        }
        bestServer.addClient(client);
    }

    public List<Server> getServers() {
        return servers;
    }
}
