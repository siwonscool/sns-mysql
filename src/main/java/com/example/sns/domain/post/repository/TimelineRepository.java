package com.example.sns.domain.post.repository;


import com.example.sns.domain.post.entity.Timeline;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TimelineRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static String TABLE = "Timeline";

    private final RowMapper<Timeline> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Timeline.builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .postId(resultSet.getLong("postId"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public List<Timeline> findAllByMemberIdAndOrderByIdDesc(Long memberId, int size) {
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", size);

        var sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY id DESC
                LIMIT :size
                """,TABLE);

        return jdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Timeline> findAllByLessThanIdANdMemberIdAndOrderByIdDesc(Long id, Long memberId, int size) {
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("id", id)
                .addValue("size", size);

        var sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId and id < :id
                ORDER BY id DESC
                LIMIT :size
                """,TABLE);

        return jdbcTemplate.query(sql, params, ROW_MAPPER);
    }


    public Timeline save(Timeline timeline) {
        if (timeline.getId() == null) {
            return this.insert(timeline);
        }

        throw new UnsupportedOperationException("post 는 갱신을 지원하지 않습니다");
    }

    public Timeline insert(Timeline timeline) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(timeline);
        var id = insert.executeAndReturnKey(params).longValue();

        return Timeline.builder()
                .id(id)
                .memberId(timeline.getMemberId())
                .postId(timeline.getPostId())
                .createdAt(timeline.getCreatedAt())
                .build();
    }

    public void bulkInsert(List<Timeline> timelines) {
        var sql = String.format("""
                INSERT INTO %s (memberId, postId, createdAt)
                VALUES (:memberId, :postId, :createdAt)
                """, TABLE);

        SqlParameterSource[] params = timelines.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, params);
    }
}
