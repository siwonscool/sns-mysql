package com.example.sns.application.usecase;

import com.example.sns.domain.follow.entity.Follow;
import com.example.sns.domain.follow.service.FollowReadService;
import com.example.sns.domain.member.dto.MemberDto;
import com.example.sns.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetFollowingMemberUsecase {

    private final MemberReadService memberReadService;
    private final FollowReadService followReadService;

    public List<MemberDto> execute(Long memberId) {
        /*
        * 1. follower memberList 조회
        * 2. 1번의 조회결과로 사용자 정보를 조회한다
        * */
        var followings = followReadService.getFollowList(memberId);
        return memberReadService.getMemberList(followings.stream().map(Follow::getToMemberId).toList());
    }
}
