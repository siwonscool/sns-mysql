package com.example.sns.application.controller;


import com.example.sns.domain.post.dto.DailyPostCountRequest;
import com.example.sns.domain.post.dto.DailyPostCountResponse;
import com.example.sns.domain.post.dto.PostCommand;
import com.example.sns.domain.post.entity.Post;
import com.example.sns.domain.post.service.PostReadService;
import com.example.sns.domain.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostWriteService postWriteService;

    private final PostReadService postReadService;

    @PostMapping
    public Long create(PostCommand command) {
        return postWriteService.create(command);
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
}
