package com.example.sns.application.usecase;

import com.example.sns.domain.follow.service.FollowWriteService;
import com.example.sns.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateFollowMemberUsecase {
    private final FollowWriteService followWriteService;
    private final MemberReadService memberReadService;

    public void execute(Long followerId, Long followeeId) {
        /*
        * 1. 입력받은 memberId 로 조회
        * 2. FollowerService create()
        * */
        var follower = memberReadService.getMember(followerId);
        var followee = memberReadService.getMember(followeeId);
        
        followWriteService.create(follower, followee);
    }

}
