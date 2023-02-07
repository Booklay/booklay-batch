package com.nhnacademy.booklay.batch.booklaybatch.dto.order;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDto {
    private Long id;
    private Long memberNo;
    private Long code;
    private LocalDateTime orderedAt;
    private Long productPriceSum;
    private Long deliveryPrice;
    private Long discountPrice;
    private Long pointUsePrice;
    private Long paymentPrice;
    private Long paymentMethod;
    private Long giftWrappingPrice;
    private Boolean isBlinded;
}
