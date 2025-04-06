package dev.Gardensong.Gamedev.service;

import dev.Gardensong.Gamedev.dto.GoogleDTO;
import dev.Gardensong.Gamedev.dto.GoogleParam;
import dev.Gardensong.Gamedev.dto.VideoDTO;
import dev.Gardensong.Gamedev.dto.VideoParam;
import dev.Gardensong.Gamedev.repository.GoogleRepository;
import dev.Gardensong.Gamedev.repository.VideoRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class GamedevService {
    private final GoogleRepository googleRepository;
    private final VideoRepository videoRepository;
    private final dev.Gardensong.Gamedev.service.OpenAIService openAIService;
    private final JSONArray conversationHistory; // ✅ 대화 이력 저장
    private static final Logger logger = LoggerFactory.getLogger(dev.Gardensong.Gamedev.service.GamedevService.class);

    @Autowired
    public GamedevService(GoogleRepository googleRepository, VideoRepository videoRepository, dev.Gardensong.Gamedev.service.OpenAIService openAIService) {
        this.googleRepository = googleRepository;
        this.videoRepository = videoRepository;
        this.openAIService = openAIService;
        this.conversationHistory = new JSONArray(); // ✅ 초기화
    }

    // ✅ 챗봇 응답을 저장하면서 호출
    public String getChatbotResponse(String fullPrompt) {
        try {
            // ✅ 사용자의 입력을 그대로 기록 (프롬프트 변형 제거)
            conversationHistory.put(new JSONObject().put("role", "user").put("content", fullPrompt));

            // ✅ OpenAI 호출
            String response = openAIService.getChatbotResponse(fullPrompt);

            // ✅ 챗봇 응답 저장
            conversationHistory.put(new JSONObject().put("role", "assistant").put("content", response));

            return response;
        } catch (Exception e) {
            logger.error("FixBot API 호출 중 오류 발생", e);
            return "⚠️ AI 응답을 가져오는 중 오류가 발생했습니다. 다시 시도해주세요.";
        }
    }


    // ✅ 최종 검색어 생성
    public String getFinalQuery() {
        return conversationHistory.toList().stream()
                .map(obj -> (JSONObject) obj)
                .filter(msg -> msg.getString("role").equals("assistant"))
                .map(msg -> msg.getString("content"))
                .reduce((first, second) -> second) // 마지막 응답 사용
                .orElse("검색어를 생성할 수 없습니다.");
    }

    // Google 검색 API 호출
    public List<GoogleDTO> getGoogleResults(String query) throws Exception {
        GoogleParam param = new GoogleParam(query);
        return googleRepository.getResults(param);
    }

    // YouTube 검색 API 호출
    public List<VideoDTO> getYouTubeResults(String query) throws Exception {
        VideoParam param = new VideoParam(query);
        return videoRepository.getVideos(param);
    }
}
