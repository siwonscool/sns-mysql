package com.example.sns.domain.member.repository;

import com.example.sns.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static String TABLE = "Member";

    private final RowMapper<Member> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Member.builder()
            .id(resultSet.getLong("id"))
            .email(resultSet.getString("email"))
            .nickname(resultSet.getString("nickname"))
            .birthday(resultSet.getObject("birthday", LocalDate.class))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public Optional<Member> findById(Long id){

        var sql = String.format("SELECT * FROM Member WHERE id = :id", TABLE);
        var param = new MapSqlParameterSource().addValue("id", id);


        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, param, ROW_MAPPER));
    }

    public List<Member> findAllByIn(List<Long> ids){
        var sql = String.format("SELECT * FROM %s WHERE id IN (:ids)", TABLE);
        var param = new MapSqlParameterSource().addValue("ids", ids);
        return jdbcTemplate.query(sql, param, ROW_MAPPER);
    }



    public Member save(Member member) {
        /*
        * member id 를 보고 갱신 또는 삽입을 정함
        * 변환값은 id 를 담아서 변환
        * */
        if(member.getId() == null) {
            return insert(member);
        }

        return update(member);
    }

    private Member insert(Member member) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("Member")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(member);
        var id = insert.executeAndReturnKey(parameterSource).longValue();
        return Member.builder()
                .id(id)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .createdAt(member.getCreatedAt())
                .build();
    }

    private Member update(Member member){
        var sql = String.format("UPDATE Member SET email = :email, nickname = :nickname, birthday = :birthday WHERE id = :id", TABLE);
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(member);
        jdbcTemplate.update(sql, parameterSource);

        return member;
    }
}
