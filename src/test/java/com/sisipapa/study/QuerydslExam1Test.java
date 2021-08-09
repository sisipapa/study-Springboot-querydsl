package com.sisipapa.study;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sisipapa.study.entity.Member;
import com.sisipapa.study.entity.QMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuerydslExam1Test {

    @Autowired
    EntityManager entityManager;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Test
    void selectJpql(){
        String qlString = "select m from Member m where m.username = :username";
        String username = "member1";
        Member findMember = entityManager.createQuery(qlString, Member.class)
                .setParameter("username", username)
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo(username);
    }

    @Test
    void selectQuerydsl(){
        String username = "member1";
        QMember m = new QMember("m");
        Member findMember = jpaQueryFactory
                .select(m)
                .from(m)
                .where(m.username.eq(username))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo(username);

    }
}
