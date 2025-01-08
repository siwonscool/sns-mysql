package com.example.sns.domain.post.service;

import com.example.sns.domain.post.dto.DailyPostCountRequest;
import com.example.sns.domain.post.dto.DailyPostCountResponse;
import com.example.sns.domain.post.entity.Post;
import com.example.sns.domain.post.repository.PostRepository;
import com.example.sns.util.CursorRequest;
import com.example.sns.util.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReadService {
    private final PostRepository postRepository;

    public List<DailyPostCountResponse> getDailyPostCount(DailyPostCountRequest request){
        /*
        * 반환값 ->  리스트 [작성일자, 작성회원, 작성 게시물 횟수]
        * */

        return postRepository.groupByCreatedDate(request);
    }

    public Page<Post> getPosts(Long memberId, Pageable pageRequest){
        return postRepository.findAllByMemberId(memberId, pageRequest);
    }

    public List<Post> getPosts(List<Long> ids){
        return postRepository.findAllByInId(ids);
    }

    public CursorResponse<Post> getPostsByMemberId(Long memberId, CursorRequest pageRequest){
        var posts = findByAll(memberId, pageRequest);
        return getNextCursorResponse(pageRequest, posts);
    }


    public CursorResponse<Post> getPostsByMemberId(List<Long> memberIds, CursorRequest pageRequest){
        var posts = findByAll(memberIds, pageRequest);
        return getNextCursorResponse(pageRequest, posts);
    }

    private static CursorResponse<Post> getNextCursorResponse(CursorRequest pageRequest, List<Post> posts) {
        var nextKey = posts.stream()
                .mapToLong(Post::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);

        return new CursorResponse<>(pageRequest.next(nextKey), posts);
    }
    private List<Post> findByAll(Long memberId, CursorRequest pageRequest) {
        if (pageRequest.hasKey()){
            return postRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(pageRequest.key(), memberId, pageRequest.size());
        }
        return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, pageRequest.size());
    }

    private List<Post> findByAll(List<Long> memberId, CursorRequest pageRequest) {
        if (pageRequest.hasKey()){
            return postRepository.findAllByInMemberLessThanIdIdAndOrderByIdDesc(pageRequest.key(), memberId, pageRequest.size());
        }
        return postRepository.findAllByInMemberIdAndOrderByIdDesc(memberId, pageRequest.size());
    }
}
