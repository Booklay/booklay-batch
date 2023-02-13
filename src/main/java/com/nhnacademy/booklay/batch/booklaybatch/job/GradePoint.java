package com.nhnacademy.booklay.batch.booklaybatch.job;

import com.nhnacademy.booklay.batch.booklaybatch.dto.member.MemberGrade;
import com.nhnacademy.booklay.batch.booklaybatch.dto.member.MemberGradeResponse;
import com.nhnacademy.booklay.batch.booklaybatch.dto.member.PointHistory;
import com.nhnacademy.booklay.batch.booklaybatch.dto.order.TotalPaymentPrice;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class GradePoint {
    private final SqlSessionFactory sessionFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int cnt = 0;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
            .start(orderStep())
            .next(gradeStep())
            .build();
    }

    @Bean
    public Step orderStep() {
        return stepBuilderFactory.get("orderStep-1")
            .<TotalPaymentPrice, MemberGrade>chunk(10)
            .reader(orderReader())
            .processor(orderProcessor())
            .writer(orderWriter())
            .build();
    }

    @Bean
    public Step gradeStep() {
        return stepBuilderFactory.get("gradetStep-1")
            .<MemberGradeResponse, PointHistory>chunk(10)
            .reader(gradeReader())
            .processor(gradeProcessor())
            .writer(gradeWriter())
            .build();
    }

    @Bean
    public MyBatisPagingItemReader<TotalPaymentPrice> orderReader() {
        return new MyBatisPagingItemReaderBuilder<TotalPaymentPrice>()
            .sqlSessionFactory(sessionFactory)
            .queryId("com.nhnacademy.booklay.batch.booklaybatch.order.mapper.OrderMapper.getOrders")
            .pageSize(10)
            .build();
    }

    @Bean
    public MyBatisPagingItemReader<MemberGradeResponse> gradeReader() {
        return new MyBatisPagingItemReaderBuilder<MemberGradeResponse>()
            .sqlSessionFactory(sessionFactory)
            .queryId(
                "com.nhnacademy.booklay.batch.booklaybatch.member.mapper.MemberGradeMapper.retrieveMemberGrades")
            .pageSize(10)
            .build();
    }

    @Bean
    public MyBatisBatchItemWriter<MemberGrade> orderWriter() {
        return new MyBatisBatchItemWriterBuilder<MemberGrade>()
            .sqlSessionFactory(sessionFactory)
            .statementId(
                "com.nhnacademy.booklay.batch.booklaybatch.member.mapper.MemberGradeMapper.insertGrade")
            .build();
    }

    @Bean
    public MyBatisBatchItemWriter<PointHistory> gradeWriter() {
        return new MyBatisBatchItemWriterBuilder<PointHistory>()
            .sqlSessionFactory(sessionFactory)
            .statementId(
                "com.nhnacademy.booklay.batch.booklaybatch.member.mapper.PointHistoryMapper.insertPoint")
            .build();
    }

    @Bean
    public ItemProcessor<TotalPaymentPrice, MemberGrade> orderProcessor() {
        return new ItemProcessor<TotalPaymentPrice, MemberGrade>() {
            @Override
            public MemberGrade process(TotalPaymentPrice totalPaymentPrice) throws Exception {
                if (totalPaymentPrice.getTotalPrice() >= 500000) {
                    return new MemberGrade(totalPaymentPrice.getMemberNo(), "플래티넘", LocalDate.now());
                }

                if (totalPaymentPrice.getTotalPrice() >= 300000) {
                    return new MemberGrade(totalPaymentPrice.getMemberNo(), "골드", LocalDate.now());
                }

                if (totalPaymentPrice.getTotalPrice() >= 100000) {
                    return new MemberGrade(totalPaymentPrice.getMemberNo(), "실버", LocalDate.now());
                }

                else {
                    return new MemberGrade(totalPaymentPrice.getMemberNo(), "화이트", LocalDate.now());
                }
            }
        };
    }

    @Bean
    public ItemProcessor<MemberGradeResponse, PointHistory> gradeProcessor() {
        return new ItemProcessor<MemberGradeResponse, PointHistory>() {
            @Override
            public PointHistory process(MemberGradeResponse grade) throws Exception {

                if (grade.getTotalPoint() == null) {
                    grade.setTotalPoint(0);
                }

                if (grade.getGrade().equals("플레티넘")) {
                    return new PointHistory(grade.getMemberNo(), 5000, grade.getTotalPoint() + 5000,
                        LocalDateTime.now(), "매월 등급 포인트 지급");
                }

                if (grade.getGrade().equals("골드")) {
                    return new PointHistory(grade.getMemberNo(), 3000, grade.getTotalPoint() + 3000,
                        LocalDateTime.now(), "매월 등급 포인트 지급");
                }

                if (grade.getGrade().equals("실버")) {
                    return new PointHistory(grade.getMemberNo(), 1000, grade.getTotalPoint() + 1000,
                        LocalDateTime.now(), "매월 등급 포인트 지급");
                }

                else {
                    return new PointHistory(grade.getMemberNo(), 100, grade.getTotalPoint(),
                        LocalDateTime.now(), "매월 등급 포인트 지급");
                }
            }
        };
    }
}
