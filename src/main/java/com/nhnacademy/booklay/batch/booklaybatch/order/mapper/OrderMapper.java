package com.nhnacademy.booklay.batch.booklaybatch.order.mapper;

import com.nhnacademy.booklay.batch.booklaybatch.dto.order.OrderDto;

public interface OrderMapper {
    OrderDto findByOrderNo(Long orderNo);
    Long totalPaymentByMemberNo(Long memberNo);
}
