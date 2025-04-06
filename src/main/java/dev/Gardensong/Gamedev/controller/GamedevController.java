// 이 클래스의 위치(패키지)를 나타냄.
package dev.Gardensong.Gamedev.controller;

// 필요앟ㄴ 클래스들을 불러오는 import
import dev.Gardensong.Gamedev.dto.GoogleDTO; //구글 검색 결과 DTO
import dev.Gardensong.Gamedev.dto.VideoDTO; //유튜브 검색 결과 DTO
import org.springframework.ui.Model;        //뷰로 데이터 전달할 때 사용하는 객체
import org.springframework.web.bind.annotation.*; //REST API용 애너테이션들
import org.springframework.http.ResponseEntity; //HTTP 응답을 쉽게 만들 수 있는 객체
import org.slf4j.Logger;                        //로그 찍는 인터페이스
import org.slf4j.LoggerFactory;                 //로그 찍는 도구

import java.util.List;                          //리스트 자료형
import java.util.Map;                           //키-값 형태로 데이터 받을 때 사용

//이 클래스가 REST API를 처리하는 컨트롤러라는 의미
@RestController
// 이 클래스에서 정의한 모든 API는 "/chat"으로 시작함
@RequestMapping("/chat")  // ✅ `/chat`이 기본 경로
 class GamedevController {

//    서비스 클래스 주입받기(Spring이 자동으로 넣어줌 - 의존성 주입)
    private final dev.Gardensong.Gamedev.service.GamedevService gamedevService;
//    로그 기록용 도구 생성
    private static final Logger logger = LoggerFactory.getLogger(dev.Gardensong.Gamedev.controller.GamedevController.class);

//    생성자를 통해 GamedevService를 받아와서 초기와
    public GamedevController(dev.Gardensong.Gamedev.service.GamedevService gamedevService) {
        this.gamedevService = gamedevService;
    }

    /**
     * 🔹 AI 응답을 가져오는 API
     * ✅ 요청 방식: POST
     * 요청 Body: JSON (message, Level, role, question 포함)
     */
    @PostMapping("/ai-response")
    public ResponseEntity<?> aiResponse(@RequestBody Map<String, Object> requestBody) {
        try {
//            요청 바디에서 데이터 꺼내기
            String userInput = (String) requestBody.get("message");
            String level = (String) requestBody.get("level");
            String role = (String) requestBody.get("role");
            String question = (String) requestBody.get("question");

            // 🔹 AI에게 전달할 최종 프롬프트 구성 ( 입력값들 조함)
            String fullPrompt = String.format("level: %s, role: %s, question: %s\n%s", level, role, question, userInput);
//            서비스에 AI 응답 요청 보내기
            String response = gamedevService.getChatbotResponse(fullPrompt);

//            성공 시 JSON 응답으로 {"answer": "AI의 답변"} 혀태로 변환
            return ResponseEntity.ok(Map.of("answer", response));
        } catch (Exception e) {
//            에러 발생 시 로그 찍고, 에러 메시지와 함께 500 응답
            logger.error("🔴 AI 응답 중 오류 발생", e);
            return ResponseEntity.status(500).body(Map.of("error", "⚠️ AI 응답을 가져오는 중 오류가 발생했습니다. 다시 시도해주세요."));
        }
    }
/**
* 검색 결과를 가져오는 API
 * 요청 URL : GET / chat/search
 * 반환 : 결과를 포함한 뷰 페이지 렌더링 (JSP, Thymeleaf 등에서 보여줄 수 있음)
 */
// ✅ `query`를 생성 후 Google & YouTube API 검색 수행
    @GetMapping("/search")
    public String search(Model model) throws Exception {

        // 1️⃣ 서비스 에서 최종 검색어 가져오기
        String finalQuery = gamedevService.getFinalQuery();
//        검색어가 비어 있으면 기본 문구로 처리
        if (finalQuery == null || finalQuery.isEmpty()) {
            finalQuery = "검색어를 생성할 수 없습니다."; // 에러 방지
        }

        // 2️⃣ Google 검색 결과 가져오기
        List<GoogleDTO> googleResults = gamedevService.getGoogleResults(finalQuery);
        model.addAttribute("googleResults", googleResults);

        // 3️⃣ YouTube 검색 결과 가져오기
        List<VideoDTO> youtubeResults = gamedevService.getYouTubeResults(finalQuery);
        model.addAttribute("youtubeResults", youtubeResults);

        // 4️⃣ 검색어도 함께 전달 (디버깅 및 사용자 확인용)
        model.addAttribute("searchQuery", finalQuery);

        return "results"; // 🚀 `/WEB-INF/views/results.jsp` 렌더링
    }
}
