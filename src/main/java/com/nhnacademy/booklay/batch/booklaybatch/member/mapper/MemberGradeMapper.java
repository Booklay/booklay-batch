package com.nhnacademy.booklay.batch.booklaybatch.member.mapper;

import com.nhnacademy.booklay.batch.booklaybatch.dto.member.MemberDto;
import com.nhnacademy.booklay.batch.booklaybatch.dto.member.MemberGradeResponse;
import java.util.List;

public interface MemberGradeMapper {
    void insertGrade();
    List<MemberGradeResponse> retrieveMemberGrades();
}
