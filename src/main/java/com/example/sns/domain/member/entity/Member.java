package com.example.sns.domain.member.entity;


import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class Member {
    private final Long id;

    private String nickname;
    private final String email;
    private final LocalDate birthday;
    private final LocalDateTime createdAt;

    private final Long NICKNAME_MAX_LENGTH = 10L;
    private final Long EMAIL_MAX_LENGTH = 50L;

    @Builder
    public Member(LocalDateTime createdAt, LocalDate birthday, String email, Long id, String nickname) {
        this.id = id;
        this.birthday = ObjectUtils.requireNonEmpty(birthday);
        this.email = ObjectUtils.requireNonEmpty(email);
        validateNickname(nickname);
        this.nickname = ObjectUtils.requireNonEmpty(nickname);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    private void validateNickname(String nickname) {
        Assert.isTrue(nickname.length() <= NICKNAME_MAX_LENGTH, "최대 닉네임 길이를 초과하였습니다");
    }

    public void changeNickname(String nickname) {
        ObjectUtils.requireNonEmpty(nickname);
        validateNickname(nickname);
        this.nickname = nickname;
    }

}
