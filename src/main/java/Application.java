import java.util.Arrays; // 배열 관련 유틸리티 클래스 Arrays를 임포트
import java.util.List;   // List 인터페이스를 임포트 (컬렉션 타입)

public class Application {

    // 워커 서버의 주소 상수 정의 (포트 8081의 작업 처리 URL)
    private static final String WORKER_ADDRESS_1 = "http://localhost:8081/tasks";
    // 워커 서버의 주소 상수 정의 (포트 8082의 작업 처리 URL)
    private static final String WORKER_ADDRESS_2 = "http://localhost:8082/tasks";

    public static void main(String[] args) { // main 메소드: 프로그램의 진입점

        Aggregator aggregator = new Aggregator(); // Aggregator 객체 생성 (여러 워커에 작업 전달 및 결과 수집)
        String task1 = "10,200"; // 첫 번째 작업 데이터 문자열 정의
        String task2 = "123456789,100000000000,7000000234343"; // 두 번째 작업 데이터 문자열 정의

        // 워커 주소와 작업 데이터를 리스트 형태로 전달하여 각 워커에 비동기 요청 후, 결과를 리스트로 수집
        List<String> results = aggregator.sendTasksToWorkers(
                Arrays.asList(WORKER_ADDRESS_1, WORKER_ADDRESS_2), // 워커 주소 리스트 생성
                Arrays.asList(task1, task2)                          // 작업 데이터 리스트 생성
        );

        // 결과 리스트의 각 요소를 순회하며 출력
        for (String result : results) { // 각 결과 문자열에 대해 반복
            System.out.println(result); // 결과 출력
        }
    }
}
