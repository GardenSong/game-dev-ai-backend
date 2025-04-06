package dev.Gardensong.Gamedev.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class GamedevPromptLoader {

    public static String loadPrompt(String fileName) {
        try {
            ClassLoader classLoader = GamedevPromptLoader.class.getClassLoader();
            URL resource = classLoader.getResource("config/"+fileName);

            if (resource == null) {
                System.err.println("❌" + fileName + "파일을 찾을 수 없습니다.");
                return "FixBot 프롬프트를 불러올 수 없습니다.";
            }

            Path path = Paths.get(resource.toURI());
            return Files.readString(path, StandardCharsets.UTF_8);

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return "FixBot 프롬프트를 불러올 수 없습니다.";
        }
    }
    public static String getFormattedPrompt(String fileName, String role, String userInput) {
        String template = loadPrompt(fileName);

        return template
                .replace("${role}", role != null ? role : "알 수 없음")
                .replace("${userInput}", userInput != null ? userInput : "질문이 제공되지 않았습니다.");

    }
}
