import networking.WebClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregator {

    private WebClient webClient;

    public Aggregator() {
        this.webClient = new WebClient();
    }

    public List<String> sendTasksToWorkers(List<String> workersAddress, List<String> tasks) {
        CompletableFuture<String> [] futures = new CompletableFuture[workersAddress.size()];

        for (int i = 0; i < workersAddress.size(); i++) {
            String workerAddress = workersAddress.get(i);
            String task = tasks.get(i);

            byte[] requestPayload = task.getBytes();

            futures[i] = webClient.sendTasks(workerAddress, requestPayload);
        }

        List<String> results = Stream.of(futures).map(CompletableFuture::join).collect(Collectors.toList());

        return results;
    }

}
