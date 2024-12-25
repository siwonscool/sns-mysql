package com.example.sns.domain.post.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record DailyPostCountRequest(
        Long memberId,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate start,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate end
) {
}
