package com.example.sns.domain.post.service;


import com.example.sns.domain.post.entity.Timeline;
import com.example.sns.domain.post.repository.TimelineRepository;
import com.example.sns.util.CursorRequest;
import com.example.sns.util.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineReadService {
    private final TimelineRepository timelineRepository;

    public CursorResponse<Timeline> getTimeLines(Long memberId, CursorRequest cursorRequest) {
        var timelines = findByAll(memberId, cursorRequest);
        var nextKey = timelines.stream()
                .mapToLong(Timeline::getId)
                .min().orElse(CursorRequest.NONE_KEY);

        return new CursorResponse<>(cursorRequest.next(nextKey), timelines);
    }

    private List<Timeline> findByAll(Long memberId, CursorRequest pageRequest) {
        if (pageRequest.hasKey()){
            return timelineRepository.findAllByLessThanIdANdMemberIdAndOrderByIdDesc(pageRequest.key(), memberId, pageRequest.size());
        }
        return timelineRepository.findAllByMemberIdAndOrderByIdDesc(memberId, pageRequest.size());
    }
}
