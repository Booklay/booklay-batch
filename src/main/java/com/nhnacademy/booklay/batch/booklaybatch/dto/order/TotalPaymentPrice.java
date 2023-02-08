package com.nhnacademy.booklay.batch.booklaybatch.dto.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TotalPaymentPrice {
    private Long memberNo;
    private Long totalPrice;
}
