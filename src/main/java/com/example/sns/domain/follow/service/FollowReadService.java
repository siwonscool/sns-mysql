package com.example.sns.domain.follow.service;


import com.example.sns.domain.follow.entity.Follow;
import com.example.sns.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowReadService {
    private final FollowRepository followRepository;

    public List<Follow> getFollowings(long memberId){
        return followRepository.findByFromMemberId(memberId);
    }


    public List<Follow> getFollowers(long memberId){
        return followRepository.findByToMemberId(memberId);
    }
}
