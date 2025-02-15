package networking; // networking 패키지에 속하는 클래스임을 선언

import java.net.URI; // 문자열 URL을 URI 객체로 변환하기 위한 클래스 임포트
import java.net.http.HttpClient; // HTTP 요청을 보내기 위한 클라이언트 클래스 임포트 (Java 11 이상)
import java.net.http.HttpRequest; // HTTP 요청을 생성하기 위한 클래스 임포트
import java.net.http.HttpResponse; // HTTP 응답을 처리하기 위한 클래스 임포트
import java.util.concurrent.CompletableFuture; // 비동기 처리를 위한 CompletableFuture 클래스 임포트

// HTTP 클라이언트를 초기화하여, 지정된 URL로 POST 요청을 보내고, 비동기적으로 응답을 받는 역할을 합니다.
public class WebClient {

    private HttpClient client; // HttpClient 인스턴스를 저장할 필드

    public WebClient() { // 생성자: WebClient 객체 초기화
        this.client = HttpClient.newBuilder() // HttpClient 빌더 시작
                .version(HttpClient.Version.HTTP_2) // HTTP/2 프로토콜 사용 설정
                .build(); // HttpClient 빌드 후 할당
    }

    /**
     * 주어진 URL로 POST 요청을 보내고, 응답 본문을 CompletableFuture로 반환하는 메소드
     *
     * @param url 요청을 보낼 대상 URL
     * @param requestPayload POST 요청에 사용할 바이트 배열 데이터
     * @return 비동기적으로 응답 본문(String)을 반환하는 CompletableFuture
     */
    public CompletableFuture<String> sendTasks(String url, byte[] requestPayload) {
        HttpRequest request = HttpRequest.newBuilder() // HttpRequest 빌더 시작
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestPayload)) // POST 요청 본문으로 바이트 배열 사용
                .uri(URI.create(url)) // 문자열 URL을 URI 객체로 변환하여 설정
                .build(); // HttpRequest 객체 빌드

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()) // 비동기 요청 전송 및 응답 본문을 String으로 처리
                .thenApply(HttpResponse::body); // 응답 객체에서 body()만 추출하여 반환
    }
}
