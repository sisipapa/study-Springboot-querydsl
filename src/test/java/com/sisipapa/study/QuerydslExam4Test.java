package com.sisipapa.study;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sisipapa.study.dto.MemberSearchDto;
import com.sisipapa.study.dto.MemberTeamDto;
import com.sisipapa.study.dto.QMemberTeamDto;
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
public class QuerydslExam4Test {

    Logger log = (Logger)LoggerFactory.getLogger(QuerydslExam4Test.class);

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Test
    void projection(){

        MemberSearchDto searchDto = MemberSearchDto.builder()
                .username("member99")
                .ageLoe(100)
                .ageGoe(90)
                .teamName("Team B")
                .build();
        BooleanBuilder builder = getBooleanBuilder(searchDto);


        List<MemberTeamDto> list = jpaQueryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();

        var usernameList = list.stream()
                .map(memberTeamDto -> memberTeamDto.getUsername())
                .collect(Collectors.toList());

        log.info("usernameList : {}", usernameList.toString());

        assertThat(usernameList.size()).isEqualTo(1);

        /*
        select
            member0_.member_id as col_0_0_,
            member0_.username as col_1_0_,
            member0_.age as col_2_0_,
            team1_.team_id as col_3_0_,
            team1_.name as col_4_0_
        from
            member member0_
        left outer join
            team team1_
                on member0_.team_id=team1_.team_id
        where
            member0_.username=?
            and team1_.name=?
            and member0_.age>=?
            and member0_.age<=?
        * */
    }

    @Test
    void paging(){

        MemberSearchDto searchDto = MemberSearchDto.builder()
                .ageLoe(100)
                .ageGoe(0)
                .teamName("Team B")
                .build();
        BooleanBuilder builder = getBooleanBuilder(searchDto);
        Pageable pageable = PageRequest.of(0, 10);

        QueryResults<MemberTeamDto> results = jpaQueryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        var usernameList = results.getResults().stream()
                .map(dto -> dto.getUsername())
                .collect(Collectors.toList());
        long total = results.getTotal();

        log.info("usernameList : {}", usernameList.toString());
        log.info("total : {}", total);

        assertThat(total).isEqualTo(50);

        /**
         * select
         *         member0_.member_id as col_0_0_,
         *         member0_.username as col_1_0_,
         *         member0_.age as col_2_0_,
         *         team1_.team_id as col_3_0_,
         *         team1_.name as col_4_0_
         *     from
         *         member member0_
         *     left outer join
         *         team team1_
         *             on member0_.team_id=team1_.team_id
         *     where
         *         team1_.name=?
         *         and member0_.age>=?
         *         and member0_.age<=?
         *     order by
         *         member0_.member_id desc limit ?
         */
    }

    BooleanBuilder getBooleanBuilder(MemberSearchDto condition){

        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(condition.getUsername())) {
            builder.and(member.username.eq(condition.getUsername()));
        }

        if (hasText(condition.getTeamName())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }

        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }

        if (condition.getAgeLoe() != null) {
            builder.and(member.age.loe(condition.getAgeLoe()));
        }

        return builder;

    }
}
