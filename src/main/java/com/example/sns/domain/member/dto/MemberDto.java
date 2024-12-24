package com.example.sns.domain.member.dto;

import com.example.sns.domain.member.entity.Member;

import java.time.LocalDate;

public record MemberDto(
        Long id,
        String email,
        String nickname,
        LocalDate birthday
) {

    public static MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getNickname(), member.getBirthday());
    }
}
