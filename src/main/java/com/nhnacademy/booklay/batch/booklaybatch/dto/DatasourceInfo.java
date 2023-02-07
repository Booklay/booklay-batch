package com.nhnacademy.booklay.batch.booklaybatch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DatasourceInfo {

    private final String username;

    private final String passwword;

    private final String dbUrl;

}
