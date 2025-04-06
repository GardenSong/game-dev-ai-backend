package dev.Gardensong.Gamedev.controller;

import dev.Gardensong.Gamedev.dto.GoogleDTO;
import dev.Gardensong.Gamedev.dto.VideoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import dev.Gardensong.Gamedev.service.GamedevService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final GamedevService gamedevService;

    @Autowired
    public SearchController(GamedevService gamedevService) {
        this.gamedevService = gamedevService;
    }

    @GetMapping("/google")
    public ResponseEntity<List<GoogleDTO>> searchGoogle(@RequestParam String query) {
        try {
            return ResponseEntity.ok(gamedevService.getGoogleResults(query));
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 찍기
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/youtube")
    public ResponseEntity<List<VideoDTO>> searchYoutube(@RequestParam String query) {
        try {
            return ResponseEntity.ok(gamedevService.getYouTubeResults(query));
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 찍기
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
