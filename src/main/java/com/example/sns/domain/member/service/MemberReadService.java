package com.example.sns.domain.member.service;

import com.example.sns.domain.member.dto.MemberDto;
import com.example.sns.domain.member.dto.MemberNicknameHistoryDto;
import com.example.sns.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.sns.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberReadService {
    private final MemberRepository memberRepository;

    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;

    public MemberDto getMember(Long id) {
        var member =  memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberDto.toDto(member);
    }

    public List<MemberDto> getMemberList(List<Long> ids) {
        if (ids.isEmpty()) return List.of();
        var members = memberRepository.findAllByIn(ids);
        return members.stream().map(MemberDto::toDto).collect(Collectors.toList());
    }

    public List<MemberNicknameHistoryDto> getMemberNicknameHistory(Long memberId){
        return memberNicknameHistoryRepository.findALLByMemberId(memberId)
                .stream()
                .map(MemberNicknameHistoryDto::toDto)
                .toList();
    }

}
