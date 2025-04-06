package dev.Gardensong.Gamedev.controller;

import dev.Gardensong.Gamedev.service.GamedevService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequestMapping("/chat")  // ✅ `/chat`이 기본 경로
public class GamedevController {
    private final GamedevService fixBotService;
    private static final Logger logger = LoggerFactory.getLogger(GamedevController.class);

    public GamedevController(GamedevService fixBotService) {
        this.fixBotService = fixBotService;
    }

    /**
     * 🔹 `/chat` 엔드포인트 (UI 제공)
     * ✅ 요청 방식: GET
     */
    @GetMapping
    public ResponseEntity<?> getChatPage(@RequestParam String level,
                                         @RequestParam String role,
                                         @RequestParam String question) {
        // 기본 데이터 반환 (JSON 형식)
        return ResponseEntity.ok(Map.of(
                "level", level,
                "role", role,
                "question", question,
                "message", "채팅을 시작하세요."
        ));
    }

    /**
     * 🔹 AI 응답을 가져오는 엔드포인트
     * ✅ 요청 방식: POST
     */
    @PostMapping("/ai-response")
    public ResponseEntity<?> aiResponse(@RequestBody Map<String, Object> requestBody) {
        try {
            String sessionId = (String) requestBody.get("sessionId");
            String userInput = (String) requestBody.get("message");
            String level = (String) requestBody.get("level");
            String role = (String) requestBody.get("role");
            String question = (String) requestBody.get("question");

            String response = fixBotService.getChatbotResponse(sessionId, userInput, level, role, question);
            return ResponseEntity.ok(Map.of("answer", response));
        } catch (Exception e) {
            logger.error("🔴 AI 응답 중 오류 발생", e);
            return ResponseEntity.status(500).body(Map.of("error", "⚠️ AI 응답을 가져오는 중 오류가 발생했습니다. 다시 시도해주세요."));
        }
    }
}