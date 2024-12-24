package com.example.sns.domain.member.dto;

import com.example.sns.domain.member.entity.MemberNicknameHistory;

import java.time.LocalDateTime;
import java.util.Optional;

public record MemberNicknameHistoryDto(
        Long id,
        Long memberId,
        String nickname,
        LocalDateTime createdAt
) {

    public static MemberNicknameHistoryDto toDto(MemberNicknameHistory memberNicknameHistory) {
        return new MemberNicknameHistoryDto(memberNicknameHistory.getId(), memberNicknameHistory.getMemberId(), memberNicknameHistory.getNickname(), memberNicknameHistory.getCreatedAt());
    }
}
