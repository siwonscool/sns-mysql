package com.example.sns.objectMother;

import com.example.sns.domain.post.entity.Post;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

import java.time.LocalDate;

public class PostFixtureFactory {

    public static EasyRandom get(Long memberId, LocalDate firstDate, LocalDate lastDate) {
        var idPredicate = FieldPredicates.named("id")
                .and(FieldPredicates.ofType(Long.class))
                .and(FieldPredicates.inClass(Post.class));

        var memberIdPredicate = FieldPredicates.named("memberId")
                .and(FieldPredicates.ofType(Long.class))
                .and(FieldPredicates.inClass(Post.class));

        var params = new EasyRandomParameters()
                .excludeField(idPredicate)
                .dateRange(firstDate, lastDate)
                .randomize(memberIdPredicate, () -> memberId);

        return new EasyRandom(params);
    }
}
