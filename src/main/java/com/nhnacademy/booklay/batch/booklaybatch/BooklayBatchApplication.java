package com.nhnacademy.booklay.batch.booklaybatch;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BooklayBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(BooklayBatchApplication.class, args);
    }
}

//배치 config 따로, 스프링 스케줄러