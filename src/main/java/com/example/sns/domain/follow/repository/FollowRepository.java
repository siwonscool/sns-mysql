package com.example.sns.domain.follow.repository;

import com.example.sns.domain.follow.entity.Follow;
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
public class FollowRepository  {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static String TABLE = "Follow";

    private final RowMapper<Follow> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Follow.builder()
            .id(resultSet.getLong("id"))
            .fromMemberId(resultSet.getLong("fromMemberId"))
            .toMemberId(resultSet.getLong("toMemberId"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public List<Follow> findByFromMemberId(Long fromMemberId){
        var sql = String.format("SELECT * FROM %s WHERE fromMemberId = :fromMemberId", TABLE);
        var param = new MapSqlParameterSource().addValue("fromMemberId", fromMemberId);
        return jdbcTemplate.query(sql, param, ROW_MAPPER);
    }


    public List<Follow> findByToMemberId(long toMemberId) {
        var sql = String.format("SELECT * FROM %s WHERE toMemberId = :toMemberId", TABLE);
        var param = new MapSqlParameterSource().addValue("toMemberId", toMemberId);
        return jdbcTemplate.query(sql, param, ROW_MAPPER);
    }

    public Follow save(Follow follow){
        if (follow.getId() == null) {
            return this.insert(follow);
        }

        throw new UnsupportedOperationException("Follow 는 갱신을 지원하지 않습니다");
    }

    private Follow insert(Follow follow){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(follow);
        var id = insert.executeAndReturnKey(params).longValue();

        return Follow.builder()
                .id(id)
                .fromMemberId(follow.getFromMemberId())
                .toMemberId(follow.getToMemberId())
                .createdAt(follow.getCreatedAt())
                .build();
    }

}
