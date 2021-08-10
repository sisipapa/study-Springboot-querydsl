package com.sisipapa.study.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberSearchDto {

    private String username;
    private String teamName;
    private Integer ageGoe;
    private Integer ageLoe;
}