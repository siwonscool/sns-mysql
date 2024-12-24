package com.example.sns.domain.member.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberNicknameHistory {
    private final Long id;
    private final LocalDateTime createdAt;
    private final Long memberId;
    private final String nickname;

    public MemberNicknameHistory(Long id, LocalDateTime createdAt, Long memberId, String nickname) {
        this.id = id;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.memberId = memberId;
        this.nickname = nickname;
    }
}
