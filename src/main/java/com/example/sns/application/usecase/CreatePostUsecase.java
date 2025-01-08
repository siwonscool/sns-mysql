package com.example.sns.application.usecase;


import com.example.sns.domain.follow.entity.Follow;
import com.example.sns.domain.follow.service.FollowReadService;
import com.example.sns.domain.post.dto.PostCommand;
import com.example.sns.domain.post.service.PostWriteService;
import com.example.sns.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreatePostUsecase {
    private final PostWriteService postWriteService;

    private final FollowReadService followReadService;

    private final TimelineWriteService timelineWriteService;

    @Transactional
    public Long execute(PostCommand postCommand) {
        var postId = postWriteService.create(postCommand);

        var followers = followReadService.getFollowers(postCommand.memberId()).stream()
                .map(Follow::getFromMemberId)
                .toList();

        timelineWriteService.deliveryToTimeline(postId, followers);

        return postId;
    }
}
