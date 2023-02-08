package com.nhnacademy.booklay.batch.booklaybatch.member.mapper;

import com.nhnacademy.booklay.batch.booklaybatch.dto.member.MemberDto;
import java.util.List;

public interface MemberMapper {
    MemberDto findByMemberNo(Long memberNo);
    List<MemberDto> getMembers();
}
