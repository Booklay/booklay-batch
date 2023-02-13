package com.nhnacademy.booklay.batch.booklaybatch.dto.member;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class MemberGradeResponse {
    private String name;
    private Long memberNo;
    private Long pointHistoryMemberNo;
    private Integer totalPoint;
    private String grade;
}
