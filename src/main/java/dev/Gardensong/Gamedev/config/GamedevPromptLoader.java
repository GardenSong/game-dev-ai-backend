package dev.Gardensong.Gamedev.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class GamedevPromptLoader {
    private static final String PROMPT_FILE_PATH = "config/Gamedev_prompt.txt";

    public static String loadPrompt() {
        try {
            ClassLoader classLoader = dev.Gardensong.Gamedev.config.GamedevPromptLoader.class.getClassLoader();
            URL resource = classLoader.getResource(PROMPT_FILE_PATH);

            if (resource == null) {
                System.err.println("❌ Gamedev_prompt.txt 파일을 찾을 수 없습니다.");
                return "Gamedev 기본 프롬프트를 불러올 수 없습니다.";
            }

            Path path = Paths.get(resource.toURI());
            return Files.readString(path, StandardCharsets.UTF_8);

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return "Gamedev 기본 프롬프트를 불러올 수 없습니다.";
        }
    }
}
