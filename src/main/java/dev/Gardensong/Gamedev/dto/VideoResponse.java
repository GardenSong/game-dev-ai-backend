package dev.Gardensong.Gamedev.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VideoResponse(VideoResult videoresult) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VideoResult(List<Video> video) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Video(String rank, String movieCd, String movieNm, String openDt, String audiAcc) {}
}