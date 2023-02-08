package com.nhnacademy.booklay.batch.booklaybatch.dto.member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class MemberDto {
    private Long memberNo;
    private Boolean genderNo;
    private String memberId;
    private String password;
    private String nickname;
    private String name;
    private LocalDate birthday;
    private String phoneNo;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean isBlocked;
}
