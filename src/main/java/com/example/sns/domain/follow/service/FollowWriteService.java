package com.example.sns.domain.follow.service;

import com.example.sns.domain.follow.entity.Follow;
import com.example.sns.domain.follow.repository.FollowRepository;
import com.example.sns.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class FollowWriteService {

    private final FollowRepository followRepository;

    public void create(MemberDto fromMember, MemberDto toMember){
        /*
        * follower, followee 들을 받아서 저장한다
        * follower, followee validation 은 두 유저는 다른유저
        * */
        Assert.isTrue(!fromMember.id().equals(toMember.id()),"팔로워, 팔로이 회원이 동일합니다");

        var follow = Follow.builder()
                .fromMemberId(fromMember.id())
                .toMemberId(toMember.id())
                .build();

        followRepository.save(follow);
    }
}
