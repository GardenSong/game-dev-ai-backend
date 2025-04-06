package dev.Gardensong.Gamedev.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.Gardensong.Gamedev.dto.GoogleDTO;
import dev.Gardensong.Gamedev.dto.GoogleParam;
import dev.Gardensong.Gamedev.dto.GoogleResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Repository
public class GoogleRepository implements APIClientRepository {
    private static final String BASE_URL = "https://www.googleapis.com/customsearch/v1";
    private final HttpClient httpClient = getHttpClient();
    private final ObjectMapper objectMapper = getObjectMapper();
    private final String API_KEY = getDotenv().get("GOOGLE_KEY"); // 환경 변수에서 API 키 가져오기
    private final String CX = getDotenv().get("GOOGLE_CX"); // 검색 엔진 ID 가져오기

    @Override
    public HttpClient getHttpClient() {
        return HttpClient.newHttpClient();
    }

    @Override
    public Dotenv getDotenv() {
        return Dotenv.configure().ignoreIfMissing().load();
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    public String callAPI(GoogleParam param) throws Exception {
        String encodedQuery = URLEncoder.encode(param.searchQuery(), StandardCharsets.UTF_8);  // ✅ URL 인코딩 적용

        String url = String.format(
                "%s?q=%s&key=%s&cx=%s&num=5",
                BASE_URL, encodedQuery, API_KEY, CX
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))  // ✅ 인코딩된 쿼리 사용
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        }
        throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
    }

    public List<GoogleDTO> getResults(GoogleParam param) throws Exception {
        String responseBody = callAPI(param);
        GoogleResponse googleResponse = objectMapper.readValue(responseBody, GoogleResponse.class);

        return googleResponse.items()
                .stream()
                .map(item -> new GoogleDTO(
                        item.title(), // 검색 결과 제목
                        item.link()   // 검색 결과 URL
                ))
                .toList();
    }
}
