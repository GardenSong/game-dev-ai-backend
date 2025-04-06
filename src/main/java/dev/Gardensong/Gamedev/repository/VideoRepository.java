package dev.Gardensong.Gamedev.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.Gardensong.Gamedev.dto.VideoDTO;
import dev.Gardensong.Gamedev.dto.VideoParam;
import dev.Gardensong.Gamedev.dto.VideoResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Repository
public class VideoRepository implements dev.Gardensong.Gamedev.repository.APIClientRepository {
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/search";
    private final HttpClient httpClient = getHttpClient();
    private final ObjectMapper objectMapper = getObjectMapper();
    private final String API_KEY = getDotenv().get("VIDEO_KEY"); // 환경 변수에서 API 키 가져오기

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

    public String callAPI(VideoParam param) throws Exception {
        String url = String.format(
                "%s?part=snippet&q=%s&key=%s&maxResults=5&type=video",
                BASE_URL, param.searchQuery(), API_KEY
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        }
        throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
    }

    public List<VideoDTO> getVideos(VideoParam param) throws Exception {
        String responseBody = callAPI(param);
        VideoResponse videoResponse = objectMapper.readValue(responseBody, VideoResponse.class);

        return videoResponse.videoresult().video()
                .stream()
                .map(v -> new VideoDTO(
                        v.movieNm(), // YouTube 동영상 제목 (movieNm → video_title로 변경)
                        v.movieCd(), // YouTube 동영상 ID (movieCd → video_id로 변경)
                        "https://www.youtube.com/watch?v=" + v.movieCd() // YouTube 동영상 URL 생성
                ))
                .toList();
    }
}
