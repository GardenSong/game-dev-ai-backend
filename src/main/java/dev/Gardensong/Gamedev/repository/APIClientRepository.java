package dev.Gardensong.Gamedev.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.http.HttpClient;

public interface APIClientRepository {
    HttpClient getHttpClient();
    Dotenv getDotenv();
    ObjectMapper getObjectMapper();
}