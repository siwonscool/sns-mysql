package com.example.sns.domain.member.service;

import com.example.sns.domain.member.dto.MemberDto;
import com.example.sns.domain.member.dto.RegisterMemberCommand;
import com.example.sns.domain.member.entity.Member;
import com.example.sns.domain.member.entity.MemberNicknameHistory;
import com.example.sns.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.sns.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;

    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;

    @Transactional
    public MemberDto create(RegisterMemberCommand command) {
        /*
        * 목표 - 회원정보를 등록한다
        * 이메일, 닉네임, 생년월일
        * 닉네임은 10자를 넘을 수 없다
        * */
        var member = Member.builder()
                .email(command.email())
                .nickname(command.nickname())
                .birthday(command.birthday())
                .build();
        var saveMember = memberRepository.save(member);
        var zero = 0/0;
        var memberNicknameHistory = MemberNicknameHistory.builder().nickname(saveMember.getNickname()).build();
        memberNicknameHistoryRepository.save(memberNicknameHistory);

        return MemberDto.toDto(saveMember);
    }

    @Transactional
    public MemberDto changeMemberNickname(Long id, String nickname) {
        var member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.changeNickname(nickname);
        memberRepository.save(member);

        var history = MemberNicknameHistory.builder().memberId(member.getId()).nickname(nickname).build();
        memberNicknameHistoryRepository.save(history);

        return MemberDto.toDto(member);
    }
}
