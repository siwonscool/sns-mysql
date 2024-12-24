package com.example.sns.application.controller;


import com.example.sns.application.usecase.CreateFollowMemberUsecase;
import com.example.sns.application.usecase.GetFollowingMemberUsecase;
import com.example.sns.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final CreateFollowMemberUsecase createFollowMemberUsecase;
    private final GetFollowingMemberUsecase getFollowingMemberUsecase;

    @PostMapping("/{fromId}/{toId}")
    public void create(@PathVariable Long fromId, @PathVariable Long toId) {
        createFollowMemberUsecase.execute(fromId, toId);
    }

    @GetMapping("/members/{fromId}")
    public List<MemberDto> get(@PathVariable Long fromId) {
        return getFollowingMemberUsecase.execute(fromId);
    }

}
