package com.example.sns.domain.follow.service;


import com.example.sns.domain.follow.entity.Follow;
import com.example.sns.domain.follow.repository.FollowRepository;
import com.example.sns.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowReadService {
    private final FollowRepository followRepository;

    public List<Follow> getFollowList(long memberId){
        return followRepository.findByFromMemberId(memberId);
    }
}
