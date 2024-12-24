package com.example.sns.domain.member.repository;

import com.example.sns.domain.member.entity.Member;
import com.example.sns.domain.member.entity.MemberNicknameHistory;
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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberNicknameHistoryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static String TABLE = "MemberNicknameHistory";

    private static final RowMapper<MemberNicknameHistory> rowMapper = (ResultSet resultSet, int rowNum) -> MemberNicknameHistory.builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .nickname(resultSet.getString("nickname"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public List<MemberNicknameHistory> findALLByMemberId(Long memberId){
        var sql = String.format("SELECT * FROM MemberNicknameHistory WHERE memberId = :memberId", TABLE);
        var param = new MapSqlParameterSource().addValue("memberId", memberId);
        return jdbcTemplate.query(sql, param, rowMapper);
    }

    public MemberNicknameHistory save(MemberNicknameHistory memberNicknameHistory) {
        /*
        * member id 를 보고 갱신 또는 삽입을 정함
        * 변환값은 id 를 담아서 변환
        * */
        if(
                memberNicknameHistory.getId() == null) {
            return insert(memberNicknameHistory);
        }

        throw new UnsupportedOperationException("갱신을 지원하지 않습니다");

    }

    private MemberNicknameHistory insert(MemberNicknameHistory memberNicknameHistory) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("MemberNicknameHistory")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(memberNicknameHistory);
        var id = insert.executeAndReturnKey(parameterSource).longValue();
        return MemberNicknameHistory.builder()
                .id(id)
                .memberId(memberNicknameHistory.getId())
                .nickname(memberNicknameHistory.getNickname())
                .createdAt(memberNicknameHistory.getCreatedAt())
                .build();
    }

}
