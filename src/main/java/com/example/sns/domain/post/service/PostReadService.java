package com.example.sns.domain.post.service;

import com.example.sns.domain.post.dto.DailyPostCountRequest;
import com.example.sns.domain.post.dto.DailyPostCountResponse;
import com.example.sns.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReadService {
    private final PostRepository postRepository;

    public List<DailyPostCountResponse> getDailyPostCount(DailyPostCountRequest request){
        /*
        * 반환값 ->  리스트 [작성일자, 작성회원, 작성 게시물 횟수]
        *
        * */

        return postRepository.groupByCreatedDate(request);
    }
}