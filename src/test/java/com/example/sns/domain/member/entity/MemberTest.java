package com.example.sns.domain.member.entity;

import com.example.sns.objectMother.MemberFixtureFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void testMember() {
        var member = MemberFixtureFactory.createMember(10L);
        var toChangeNickname = "test";

        member.changeNickname(toChangeNickname);

        Assertions.assertEquals(toChangeNickname, member.getNickname());
    }

    /**
     * Test class for the Member entity.
     * This class contains tests for the changeNickname method to validate its behavior.
     * <p>
     * Method being tested:
     * public void changeNickname(String nickname)
     * <p>
     * Use case: This method allows updating the 'nickname' field,
     * ensuring that the provided nickname is non-null and within the allowed length constraints.
     */

    @Test
    void testChangeNicknameSuccessfully() {
        // Arrange
        Member member = Member.builder()
                .id(1L)
                .nickname("OldNick")
                .email("example@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();
        String newNickname = "NewNick";

        // Act
        member.changeNickname(newNickname);

        // Assert
        assertEquals(newNickname, member.getNickname());
    }

    @Test
    void testChangeNicknameThrowsWhenNull() {
        // Arrange
        Member member = Member.builder()
                .id(1L)
                .nickname("OldNick")
                .email("example@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();
        String newNickname = null;

        // Assert
        assertThrows(NullPointerException.class, () -> {
            // Act
            member.changeNickname(newNickname);
        });
    }

    @Test
    void testChangeNicknameThrowsWhenExceedsMaxLength() {
        // Arrange
        Member member = Member.builder()
                .id(1L)
                .nickname("OldNick")
                .email("example@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();
        String newNickname = "NicknameTooLong";

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            // Act
            member.changeNickname(newNickname);
        });
    }

    @Test
    void testChangeNicknameTrimsValidNickname() {
        // Arrange
        Member member = Member.builder()
                .id(1L)
                .nickname("OldNick")
                .email("example@mail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();
        String newNickname = "ValidNick";

        // Act
        member.changeNickname(newNickname);

        // Assert
        assertEquals(newNickname, member.getNickname());
    }
}