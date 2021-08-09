package com.sisipapa.study;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sisipapa.study.entity.Member;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.sisipapa.study.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuerydslExam2Test {

    Logger log = (Logger) LoggerFactory.getLogger(QuerydslExam2Test.class);

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Test
    void fetch(){
        List<Member> members = jpaQueryFactory
                .selectFrom(member)
                .fetch();

        assertThat(members.stream().count()).isEqualTo(100);
        log.info("### list size : {}", members.stream().count());
    }

    @Test
    void fetchOne(){
        Member findMember = jpaQueryFactory
                .selectFrom(member)
                .where(member.age.eq(99), member.username.eq("member99"))
                .fetchOne();

        log.info("### age : {}", findMember.getAge());
        log.info("### username : {}", findMember.getUsername());
        assertThat(findMember.getAge()).isEqualTo(99);
        assertThat(findMember.getUsername()).isEqualTo("member99");
    }

    @Test
    void fetchFirst(){
        Member findMember = jpaQueryFactory
                .selectFrom(member)
                .where(member.team.id.eq(2l))
                .fetchFirst();

        log.info("username : {}", findMember.getUsername());
        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    void fetchResults(){
        QueryResults<Member> results = jpaQueryFactory
                .selectFrom(member)
                .fetchResults();

        results.getResults()
                .stream()
                .forEach(eachMember -> {
                        log.info("### username : {}", eachMember.getUsername());
                });

        log.info("total count : {}", results.getTotal());
        assertThat(results.getTotal()).isEqualTo(100);
    }

    @Test
    void fetchCount(){
        long totalCount = jpaQueryFactory
                .selectFrom(member)
                .fetchCount();

        log.info("total count : {}", totalCount);
        assertThat(totalCount).isEqualTo(100);
    }
}
