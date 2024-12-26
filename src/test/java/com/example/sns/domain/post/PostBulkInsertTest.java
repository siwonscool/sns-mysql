package com.example.sns.domain.post;


import com.example.sns.domain.post.entity.Post;
import com.example.sns.domain.post.repository.PostRepository;
import com.example.sns.objectMother.PostFixtureFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsertTest {
    @Autowired
    private PostRepository postRepository;


    @Test
    void 벌크_insert(){
        var easyRandom = PostFixtureFactory.get(
                5L,
                LocalDate.of(2023,1,1),
                LocalDate.of(2023,12,31)
        );

        var stopWatch = new StopWatch();
        stopWatch.start();

        var posts = IntStream.range(0, 10000 * 100)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .toList();
        stopWatch.stop();

        System.out.println("객체 생성시간 : " + stopWatch.getTotalTimeSeconds() );

        var queryStopWatch = new StopWatch();
        queryStopWatch.start();
        postRepository.bulkInsert(posts);
        queryStopWatch.stop();
        System.out.println("DB 인서트 시간 : " + queryStopWatch.getTotalTimeSeconds() );
    }
}
