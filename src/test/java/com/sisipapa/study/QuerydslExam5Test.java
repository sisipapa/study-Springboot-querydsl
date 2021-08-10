package com.sisipapa.study;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sisipapa.study.dto.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static com.sisipapa.study.entity.QMember.member;
import static com.sisipapa.study.entity.QTeam.team;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.StringUtils.hasText;

@SpringBootTest
public class QuerydslExam5Test {

    Logger log = (Logger)LoggerFactory.getLogger(QuerydslExam5Test.class);

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    /**
     * select 사용
     */
    @Test
    void projection1(){
        List<String> result = jpaQueryFactory
                .select(member.username)
                .from(member)
                .fetch();

        log.info("result : {}", result.toString());
    }

    @Test
    void projection2(){
        List<Tuple> result = jpaQueryFactory
                .select(member.username, member.age)
                .from(member)
                .orderBy(member.age.asc())
                .fetch();

        var resultList = result.stream()
                .map(tuple -> tuple.get(0, String.class))
                .collect(Collectors.toList());

        log.info("resultList : {}", resultList);
    }

    @Test
    void projection3(){
        List<MemberDto> result = jpaQueryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        var resultList = result.stream()
                .map(dto -> dto.getUsername())
                .collect(Collectors.toList());

        log.info("resultList : {}", resultList);
    }

    @Test
    void projection4(){
        List<MemberDto> result = jpaQueryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        var resultList = result.stream()
                .map(dto -> dto.getUsername())
                .collect(Collectors.toList());

        log.info("resultList : {}", resultList);
    }

    @Test
    void projection5(){
        List<MemberDto> result = jpaQueryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        var resultList = result.stream()
                .map(dto -> dto.getUsername())
                .collect(Collectors.toList());

        log.info("resultList : {}", resultList);
    }

    @Test
    void projection6(){
        List<MemberDto> result = jpaQueryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        var resultList = result.stream()
                .map(dto -> dto.getUsername())
                .collect(Collectors.toList());

        log.info("resultList : {}", resultList);
    }
}
