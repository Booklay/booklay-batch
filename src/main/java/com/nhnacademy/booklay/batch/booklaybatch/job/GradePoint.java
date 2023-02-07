package com.nhnacademy.booklay.batch.booklaybatch.job;

import com.nhnacademy.booklay.batch.booklaybatch.dto.member.MemberDto;
import com.nhnacademy.booklay.batch.booklaybatch.member.mapper.MemberMapper;
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
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class GradePoint {
    private final SqlSessionFactory sessionFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PlatformTransactionManager transactionManager;
    private final MemberMapper memberMapper;
    @Bean
    public Job job(){
        return jobBuilderFactory.get("job")
            .start(sampleStep())
            .build();
    }
    //TODO: 구현하기
    @Bean
    public Step orderStep(){
        return stepBuilderFactory.get("orderStep")
            .<MemberDto, MemberDto> chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(itemWriter())
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
    public MyBatisPagingItemReader<MemberDto> reader() {
        return new MyBatisPagingItemReaderBuilder<MemberDto>()
            .sqlSessionFactory(sessionFactory)
            .queryId("com.nhnacademy.booklay.batch.booklaybatch.member.mapper.MemberMapper.getMembers")
            .pageSize(10)
            .build();
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
}
