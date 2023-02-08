package com.nhnacademy.booklay.batch.booklaybatch.dto.member;

import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class MemberGrade {
    private final Long memberNo;
    private final String name;
    private final LocalDate date;
}
