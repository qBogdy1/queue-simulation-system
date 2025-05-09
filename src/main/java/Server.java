
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server extends Thread {
    private final BlockingQueue<Client> queue = new LinkedBlockingQueue<>();
    private int waitingPeriod = 0;

    public void addClient(Client client) {
        queue.add(client);
        waitingPeriod += client.getServiceTime();
    }

    public int getWaitingPeriod() {
        return waitingPeriod;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public BlockingQueue<Client> getQueue() {
        return queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Client client = queue.peek();
                if (client != null) {
                    Thread.sleep(client.getServiceTime() * 1000L);
                    queue.remove();
                    waitingPeriod -= client.getServiceTime();
                } else {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
