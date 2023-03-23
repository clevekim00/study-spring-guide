package me.clevekim.springguide.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
class BatchConfiguration {

    //reader() creates an ItemReader. It looks for a file called sample-data.csv and parses each line item with enough information to turn it into a Person.
    @Bean
    fun reader(): FlatFileItemReader<Person?>? {
        return FlatFileItemReaderBuilder<Person>()
            .name("personItemReader")
            .resource(ClassPathResource("sample-data.csv"))
            .delimited()
            .names(*arrayOf("firstName", "lastName"))
            .fieldSetMapper(PersonMapper())
            .build()
    }

    //processor() creates an instance of the PersonItemProcessor that you defined earlier, meant to convert the data to upper case.
    @Bean
    fun processor(): PersonItemProcessor? {
        return PersonItemProcessor()
    }

    //writer(DataSource) creates an ItemWriter.
    // This one is aimed at a JDBC destination and automatically gets a copy of the dataSource created by @EnableBatchProcessing.
    // It includes the SQL statement needed to insert a single Person, driven by Java bean properties.
    @Bean
    fun writer(dataSource: DataSource?): JdbcBatchItemWriter<Person>? {
        return dataSource?.let {
            JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(it)
                .build()
        }
    }

    @Bean
    fun importUserJob(
        jobRepository: JobRepository?,
        listener: JobCompletionNotificationListener?, step1: Step?
    ): Job? {
        return jobRepository?.let {
            JobBuilder("importUserJob", it)
                .incrementer(RunIdIncrementer())
                .listener(listener!!)
                .flow(step1!!)
                .end()
                .build()
        }
    }

    @Bean
    fun step1(
        jobRepository: JobRepository?,
        transactionManager: PlatformTransactionManager?, writer: JdbcBatchItemWriter<Person?>?
    ): Step? {
        return processor()?.let {
            StepBuilder("step1", jobRepository!!)
                .chunk<Person?, Person?>(10, transactionManager!!)
            .reader(reader()!!)
                .processor(it)
                .writer(writer!!)
                .build()
        }
    }
}