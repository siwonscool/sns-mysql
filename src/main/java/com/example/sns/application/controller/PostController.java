package com.example.sns.application.controller;


import com.example.sns.application.usecase.CreatePostUsecase;
import com.example.sns.application.usecase.GetTimelinePostUsecase;
import com.example.sns.domain.post.dto.DailyPostCountRequest;
import com.example.sns.domain.post.dto.DailyPostCountResponse;
import com.example.sns.domain.post.dto.PostCommand;
import com.example.sns.domain.post.entity.Post;
import com.example.sns.domain.post.service.PostReadService;
import com.example.sns.util.CursorRequest;
import com.example.sns.util.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostReadService postReadService;
    private final GetTimelinePostUsecase getTimelinePostUsecase;
    private final CreatePostUsecase createPostUsecase;

    @PostMapping
    public Long create(PostCommand command) {
        return createPostUsecase.execute(command);
    }

    @GetMapping("daily-post-count")
    public List<DailyPostCountResponse> getDailyPostCount(DailyPostCountRequest request) {
        return postReadService.getDailyPostCount(request);
    }

    @GetMapping("/members/{memberId}")
    public Page<Post> getPosts(@PathVariable("memberId") Long memberId,
                               Pageable pageRequest) {
        return postReadService.getPosts(memberId, pageRequest);
    }

    @GetMapping("/members/{memberId}/by-cursor")
    public CursorResponse<Post> getPosts(
            @PathVariable Long memberId,
            CursorRequest cursorRequest){
        return postReadService.getPostsByMemberId(memberId, cursorRequest);
    }

    @GetMapping("/members/{memberId}/timeline")
    public CursorResponse<Post> getPostTimeline(
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ){
        return getTimelinePostUsecase.executeTimeline(memberId, cursorRequest);
    }
}
