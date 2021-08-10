package com.sisipapa.study;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sisipapa.study.entity.Member;
import com.sisipapa.study.entity.QMember;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.sisipapa.study.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuerydslExam6Test {

    Logger log = (Logger)LoggerFactory.getLogger(QuerydslExam6Test.class);

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Test
    void subquery(){
        QMember subQueryMember = new QMember("test");
        Member findMember = jpaQueryFactory
                .selectFrom(member)
                .where(member.age.eq( JPAExpressions.select(subQueryMember.age.max()) .from(subQueryMember) ))
                .fetchOne();

        log.info("username : {}" ,findMember.getUsername());
        assertThat(findMember.getUsername()).isEqualTo("member99");


        /**
         * select
         *         member0_.member_id as member_i1_0_,
         *         member0_.age as age2_0_,
         *         member0_.team_id as team_id4_0_,
         *         member0_.username as username3_0_
         *     from
         *         member member0_
         *     where
         *         member0_.age=(
         *             select
         *                 max(member1_.age)
         *             from
         *                 member member1_
         *         )
         */
    }

    @Test
    void caseSelect(){
        List<String> result = jpaQueryFactory
                .select(member.age .when(10).then("TEN") .when(20).then("TWENTY") .otherwise("ETC"))
                .from(member)
                .fetch();

        log.info("result : {}", result.toString());
    }

    @Test
    void constant(){
        List<Tuple> result = jpaQueryFactory
                .select(member.username, Expressions.constant("TEST"))
                .from(member)
                .fetch();

        log.info("result : {}", result.toString());
    }

    @Test
    void concat(){
        String result = jpaQueryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member99"))
                .fetchOne();

        log.info("result : {}", result);
    }
}
