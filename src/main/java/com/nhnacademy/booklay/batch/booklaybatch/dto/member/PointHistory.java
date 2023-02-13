package com.nhnacademy.booklay.batch.booklaybatch.dto.member;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class PointHistory {
    private final Long memberNo;
    private final Integer point;
    private final Integer totalPoint;
    private final LocalDateTime updatedAt;
    private final String updatedDetail;
}
