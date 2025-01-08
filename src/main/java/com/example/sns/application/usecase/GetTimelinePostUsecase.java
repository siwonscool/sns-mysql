package com.example.sns.application.usecase;

import com.example.sns.domain.follow.entity.Follow;
import com.example.sns.domain.follow.service.FollowReadService;
import com.example.sns.domain.post.entity.Post;
import com.example.sns.domain.post.entity.Timeline;
import com.example.sns.domain.post.service.PostReadService;
import com.example.sns.domain.post.service.TimelineReadService;
import com.example.sns.util.CursorRequest;
import com.example.sns.util.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GetTimelinePostUsecase {
    private final PostReadService postReadService;
    private final FollowReadService followReadService;
    private final TimelineReadService timelineReadService;

    public CursorResponse<Post> execute(Long memberId, CursorRequest cursorRequest) {
        /*
        * 1. memberId -> follow 조회
        * 2. 1번 결과물로 게시물 조회
        * */

        var followings = followReadService.getFollowings(memberId);
        var followingMemberIds = followings.stream().map(Follow::getToMemberId).toList();

        return postReadService.getPostsByMemberId(followingMemberIds, cursorRequest);
    }

    public CursorResponse<Post> executeTimeline(Long memberId, CursorRequest cursorRequest) {
        /*
         * 1. Timeline 조회
         * 2. 1번에 해당하는 게시물 조회
         * */

        var timeLines = timelineReadService.getTimeLines(memberId, cursorRequest);
        var followingMemberIds = timeLines.body().stream().map(Timeline::getPostId).toList();
        var posts = postReadService.getPosts(followingMemberIds);

        return new CursorResponse<>(timeLines.nextCursorRequest(), posts);
    }

}

