package com.nhnacademy.booklay.batch.booklaybatch.job;

import com.nhnacademy.booklay.batch.booklaybatch.dto.member.MemberDto;
import com.nhnacademy.booklay.batch.booklaybatch.dto.member.MemberGrade;
import com.nhnacademy.booklay.batch.booklaybatch.dto.order.TotalPaymentPrice;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class GradePoint {
    private final SqlSessionFactory sessionFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    @Bean
    public Job job(){
        return jobBuilderFactory.get("job")
            .start(orderStep())
            .build();
    }
    @Bean
    public Step sampleStep(){
        return stepBuilderFactory.get("sampleStep-1")
            .<MemberDto, MemberDto> chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(itemWriter())
            .build();
    }
    @Bean
    public Step orderStep(){
        return stepBuilderFactory.get("orderStep-1")
            .<TotalPaymentPrice, MemberGrade> chunk(10)
            .reader(orderReader())
            .processor(orderProcessor())
            .writer(orderWriter())
            .build();
    }

    @Bean
    public MyBatisPagingItemReader<MemberDto> reader() {
        return new MyBatisPagingItemReaderBuilder<MemberDto>()
            .sqlSessionFactory(sessionFactory)
            .queryId("com.nhnacademy.booklay.batch.booklaybatch.member.mapper.MemberMapper.getMembers")
            .pageSize(10)
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
    public ItemWriter<MemberDto> itemWriter() {
//        return new MyBatisBatchItemWriterBuilder<MemberDto>()
//            .sqlSessionFactory(sessionFactory)
//            .statementId("")
//            .build();
        return members -> {
            for(MemberDto member : members) {
                log.error("=================================item-writer:{}", member.toString());
            }
        };
    }

    @Bean
    public ItemWriter<MemberGrade> orderWriter() {
        return grades -> {
            for(MemberGrade grade : grades) {
                log.error("=================================item-writer:{}", grade);
            }
        };
    }


    @Bean
    public ItemProcessor<MemberDto, MemberDto> processor() {
        return new ItemProcessor<MemberDto, MemberDto>() {
            @Override
            public MemberDto process(MemberDto memberDto) throws Exception {
                log.error("================================item-process:{}", memberDto);
                return memberDto;
            }
        };
    }

    @Bean
    public ItemProcessor<TotalPaymentPrice, MemberGrade> orderProcessor() {
        return new ItemProcessor<TotalPaymentPrice, MemberGrade>() {
            @Override
            public MemberGrade process(TotalPaymentPrice totalPaymentPrice) throws Exception {
                log.error("================================item-process:{}", totalPaymentPrice);

                if (totalPaymentPrice.getTotalPrice() >= 500000) {
                    return new MemberGrade(totalPaymentPrice.getMemberNo(), "플래티넘", LocalDate.now());}

                if (totalPaymentPrice.getTotalPrice() >= 300000) {
                    return new MemberGrade(totalPaymentPrice.getMemberNo(), "골드", LocalDate.now());}

                if (totalPaymentPrice.getTotalPrice() >= 100000) {
                    return new MemberGrade(totalPaymentPrice.getMemberNo(), "실버", LocalDate.now());}

                else {
                    return new MemberGrade(totalPaymentPrice.getMemberNo(), "화이트", LocalDate.now());}
            }
        };
    }

}
