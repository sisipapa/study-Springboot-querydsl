package com.sisipapa.study;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sisipapa.study.entity.Member;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static com.sisipapa.study.entity.QMember.member;
import static com.sisipapa.study.entity.QTeam.team;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuerydslExam3Test {

    Logger log = (Logger)LoggerFactory.getLogger(QuerydslExam3Test.class);

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Test
    void orderBy(){
        List<Member> findMembers = jpaQueryFactory
                .selectFrom(member)
                .where(member.team.id.eq(2l))
                .orderBy(member.age.desc().nullsLast())
                .fetch();

        findMembers.stream().forEach(findMember -> {
            log.info("username : {}, age : {}", findMember.getUsername(), findMember.getAge());
        });
    }

    @Test
    void paging(){
        List<Member> findMembers = jpaQueryFactory
                .selectFrom(member)
                .orderBy(member.id.desc())
                .offset(0)
                .limit(10)
                .fetch();

        var membersUsername = findMembers.stream()
                .map(findMember -> findMember.getUsername())
                .collect(Collectors.toList());

        log.info("member username : {}", membersUsername.toString());
    }

    @Test
    void groupBy(){
        List<Tuple> findMembers = jpaQueryFactory
                .select(team.name, member.age.max())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = findMembers.get(0);
        log.info("team name : {}", teamA.get(0, String.class));
        log.info("team age max : {}", teamA.get(1, Long.class));

        assertThat(teamA.get(team.name)).isEqualTo("Team A");
        assertThat(teamA.get(member.age.max())).isEqualTo(98);

    }

}
