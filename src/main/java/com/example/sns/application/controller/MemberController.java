package com.example.sns.application.controller;


import com.example.sns.domain.member.dto.MemberDto;
import com.example.sns.domain.member.dto.MemberNicknameHistoryDto;
import com.example.sns.domain.member.dto.RegisterMemberCommand;
import com.example.sns.domain.member.service.MemberReadService;
import com.example.sns.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberWriteService memberWriteService;

    private final MemberReadService memberReadService;

    @PostMapping("/members")
    public MemberDto createMember(@RequestBody RegisterMemberCommand command) {
        return memberWriteService.create(command);
    }

    @GetMapping("/members/{id}")
    public MemberDto getMember(@PathVariable Long id) {
        return memberReadService.getMember(id);
    }

    @PutMapping("/members/{id}")
    public MemberDto updateMember(@PathVariable Long id, @RequestParam String nickname) {
        return memberWriteService.changeMemberNickname(id, nickname);
    }

    @GetMapping("/{memberId}/histories")
    private List<MemberNicknameHistoryDto> getNicknameHistories(@PathVariable Long memberId){
        return memberReadService.getMemberNicknameHistory(memberId);
    }
}
