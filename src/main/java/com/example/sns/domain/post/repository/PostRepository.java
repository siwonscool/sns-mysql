package com.example.sns.domain.post.repository;

import com.example.sns.domain.post.dto.DailyPostCountRequest;
import com.example.sns.domain.post.dto.DailyPostCountResponse;
import com.example.sns.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static String TABLE = "Post";

    private final RowMapper<DailyPostCountResponse> DAILY_POST_COUNT_MAPPER = (ResultSet resultSet, int rowNum) -> new DailyPostCountResponse (
            resultSet.getLong("memberId"),
            resultSet.getObject("createdDate",LocalDate.class),
            resultSet.getLong("count")
    );

    public List<DailyPostCountResponse> groupByCreatedDate(DailyPostCountRequest request){
        var sql = String.format("""
                SELECT memberId, createdDate, COUNT(*) AS count
                FROM %s
                WHERE memberId = :memberId
                AND createdDate BETWEEN :start AND :end
                GROUP BY memberId, createdDate
                """, TABLE);

        var param = new BeanPropertySqlParameterSource(request);
        return jdbcTemplate.query(sql, param, DAILY_POST_COUNT_MAPPER);
    }


    public Post save(Post post){
        if (post.getId() == null) {
            return this.insert(post);
        }

        throw new UnsupportedOperationException("post 는 갱신을 지원하지 않습니다");
    }

    public void bulkInsert(List<Post> posts){
        var sql = String.format("""
                INSERT INTO %s (memberId, contents, createdDate, createdAt)
                VALUES (:memberId, :contents, :createdDate, :createdAt)
                """, TABLE);

        SqlParameterSource[] params = posts
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, params);
    }

    private Post insert(Post post){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        var id = insert.executeAndReturnKey(params).longValue();

        return Post.builder()
                .id(id)
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdAt(post.getCreatedAt())
                .createdDate(post.getCreatedDate())
                .build();
    }
}
