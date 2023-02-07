package com.nhnacademy.booklay.batch.booklaybatch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecretResponse {
    private Header header;
    private Body body;
}