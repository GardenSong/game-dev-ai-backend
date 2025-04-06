package dev.Gardensong.Gamedev.service;

import dev.Gardensong.Gamedev.config.GamedevPromptLoader;
import dev.Gardensong.Gamedev.dto.*;
import dev.Gardensong.Gamedev.repository.GoogleRepository;
import dev.Gardensong.Gamedev.repository.VideoRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GamedevService {
    private final GoogleRepository googleRepository;
    private final VideoRepository videoRepository;
    private final OpenAIService openAIService;
    private static final Logger logger = LoggerFactory.getLogger(GamedevService.class);
    private final Map<String, JSONArray> sessionHistories = new ConcurrentHashMap<>();


    @Autowired
    public GamedevService(GoogleRepository googleRepository, VideoRepository videoRepository, OpenAIService openAIService) {
        this.googleRepository = googleRepository;
        this.videoRepository = videoRepository;
        this.openAIService = openAIService;
    }

    // ✅ 챗봇 응답을 저장하면서 호출
    public String getChatbotResponse(String sessionId, String userInput, String level, String role, String question) {
        try {
            // ✅ system prompt는 매번 새로 넣어줘야 함
            String systemPrompt = GamedevPromptLoader.getFormattedPrompt("Gamedev_prompt.txt", null, null);
            JSONArray conversationHistory = sessionHistories.computeIfAbsent(sessionId, k -> {
                JSONArray arr = new JSONArray();
                arr.put(new JSONObject().put("role", "system").put("content", systemPrompt));
                return arr;
            });

            // ✅ structuredInput: 사용자의 질문 + 컨텍스트 정리
            String structuredInput = String.format(
                    "직급: %s\n역할: %s\n질문: %s\n사용자 채팅: %s",
                    level, role, question, userInput
            );

             conversationHistory.put(new JSONObject()
                    .put("role", "user")
                    .put("content", structuredInput));

            String response = openAIService.getChatbotResponse(conversationHistory);

            conversationHistory.put(new JSONObject()
                    .put("role", "assistant")
                    .put("content", response));

            return response;
        } catch (Exception e) {
            logger.error("FixBot API 호출 중 오류 발생", e);
            return "⚠️ AI 응답을 가져오는 중 오류가 발생했습니다. 다시 시도해주세요.";
        }
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