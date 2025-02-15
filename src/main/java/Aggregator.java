import networking.WebClient; // networking 패키지의 WebClient 클래스를 사용하기 위해 임포트

import java.util.List; // List 인터페이스를 사용하기 위해 임포트 (컬렉션 타입)
import java.util.concurrent.CompletableFuture; // CompletableFuture 클래스를 사용하여 비동기 결과를 처리하기 위해 임포트
import java.util.stream.Collectors; // 스트림 결과를 List 등으로 수집하기 위해 임포트 (Collectors 클래스)
import java.util.stream.Stream; // 배열이나 컬렉션을 스트림으로 변환하기 위해 임포트 (Stream 인터페이스)

// 여러 워커(worker) 서버에 각각 POST 요청을 보내고, 그 응답을 모아 하나의 결과 리스트로 반환하는 역할을 합니다.
public class Aggregator {

    private WebClient webClient; // WebClient 인스턴스를 저장할 필드

    public Aggregator() { // 생성자: Aggregator 초기화
        this.webClient = new WebClient(); // WebClient 인스턴스 생성 후 할당
    }

    /**
     * 주어진 워커 주소와 작업(task) 리스트를 사용해 각 워커에 비동기 POST 요청을 보내고,
     * 응답 결과들을 수집하여 반환하는 메소드
     *
     * @param workersAddress 워커 서버의 주소 리스트 (예: "http://localhost:8081/tasks")
     * @param tasks 각 워커에게 보낼 작업 데이터를 담은 문자열 리스트
     * @return 모든 워커로부터 받은 응답 결과의 리스트
     */
    public List<String> sendTasksToWorkers(List<String> workersAddress, List<String> tasks) {
        CompletableFuture<String>[] futures = new CompletableFuture[workersAddress.size()]; // 워커 수만큼 CompletableFuture 배열 생성

        for (int i = 0; i < workersAddress.size(); i++) { // 모든 워커에 대해 반복 처리
            String workerAddress = workersAddress.get(i); // 현재 워커의 주소 가져오기
            String task = tasks.get(i); // 현재 워커에 해당하는 작업 문자열 가져오기

            byte[] requestPayload = task.getBytes(); // 작업 문자열을 바이트 배열로 변환 (HTTP 요청 본문)

            futures[i] = webClient.sendTasks(workerAddress, requestPayload); // WebClient를 통해 비동기 POST 요청 전송 후 CompletableFuture 저장

            // 커넥션 풀링을 확인하기 위한 sleep time
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        List<String> results = Stream.of(futures) // CompletableFuture 배열을 스트림으로 변환
                .map(CompletableFuture::join) // 각 CompletableFuture의 결과를 동기적으로 가져옴
                .collect(Collectors.toList()); // 결과를 List로 수집

        return results; // 수집된 결과 리스트 반환
    }
}
