package com.example.springbatchexample.config;

import com.example.springbatchexample.domain.Author;
import com.example.springbatchexample.domain.Book;
import com.example.springbatchexample.domain.Genre;
import com.example.springbatchexample.dto.BookResponseDto;
import com.example.springbatchexample.repository.BookJpaRepository;
import com.example.springbatchexample.repository.BookRepository;
import com.example.springbatchexample.service.CleanUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class JobConfig {
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final BookRepository bookRepository;
    private final CleanUpService cleanUpService;
    private final DataSource dataSource;
    private final BookJpaRepository bookJpaRepository;

    @Bean
    public Job job() {
        return jobBuilderFactory
                .get("migrationJob")
                .incrementer(new RunIdIncrementer())
                .flow(bookMigrationStep())
                .next(cleanUpStep())
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        System.out.println("Начало выполнения Job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        System.out.println("Job закончена");
                    }
                })
                .build();
    }

    public Step bookMigrationStep() {
        return stepBuilderFactory
                .get("migrationStep")
                .<BookResponseDto, Book>chunk(5)
                .reader(reader())
                .processor(processor())
                // при возникновении ошибки выполнение не прерывается
                .faultTolerant()
                .writer(writer())
                // Шаг может выполниться только один раз. Чтобы выполнять его неоднократно:
                .allowStartIfComplete(true)
                .listener(new ItemWriteListener<>() {
                    public void beforeWrite(@NonNull List list) {
                        System.out.println("Подготовка к записи пакета размером: " + list.size());
                    }

                    public void afterWrite(@NonNull List list) {
                        System.out.printf("Chunk размером %s записан \n", list.size());
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List list) {
                        e.printStackTrace();
                    }
                })
                .listener(new ItemProcessListener<>() {
                    @Override
                    public void beforeProcess(BookResponseDto item) {
                        System.out.println("Подготовка к обработке");
                    }

                    @Override
                    public void afterProcess(BookResponseDto item, Book result) {
                        System.out.printf("Обработка завершена %s -> %s", item.getTitle(), result.getTitle() + "\n");
                    }

                    @Override
                    public void onProcessError(BookResponseDto item, Exception e) {
                        System.out.println("Ошибка обработки в книге с id = " + item.getId());
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        System.out.println("Chunk начинает обрабатываться");
                    }

                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        System.out.println("Chunk обработан");
                    }

                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        System.out.println("Ошибка в Chunk " + chunkContext);
                    }
                })
                .build();
    }

    // Если указать метод findAll, то выкинет LazyInitializationException в Процессоре
    // Видимо, потому, что когда сущности доходят до него, они уже отключены от контекста
    public RepositoryItemReader<BookResponseDto> reader() {
        return new RepositoryItemReaderBuilder<BookResponseDto>()
                .name("findAllInitialized")
                .sorts(new HashMap<>())
                .repository(bookJpaRepository)
                .methodName("findAllInitialized")
                .build();
    }

    // Либо можно использовать jdbc
//    @Bean
//    public JdbcPagingItemReader<BookJpa> reader() {
//        // По названию книги в обратном порядке - так сущности будут читаться из БД
//        Map<String, Order> map = new HashMap<>();
//        map.put("book_name", Order.DESCENDING);
//
//        return new JdbcPagingItemReaderBuilder<BookJpa>()
//                .name("findAll")
//                .selectClause("select " +
//                        "b.book_id, " +
//                        "b.book_name, " +
//                        "a.author_surname, " +
//                        "a.author_name, " +
//                        "g.genre_name ")
//                .fromClause("from books b " +
//                        "left join authors a on b.author_id=a.author_id " +
//                        "left join genres g on b.genre_id=g.genre_id")
//                .sortKeys(map)
//                .rowMapper((rs, rowNo) -> new BookJpa(
//                        rs.getLong("book_id"),
//                        rs.getString("book_name"),
//                        new AuthorJpa(null, rs.getString("author_surname"),
//                                rs.getString("author_name")),
//                        new GenreJpa(null, rs.getString("genre_name"))))
//                .dataSource(dataSource)
//                .build();
//    }

    public ItemProcessor<BookResponseDto, Book> processor() {
        return book -> new Book(null, book.getTitle(),
                new Author(null, book.getAuthorSurname(), book.getAuthorName()),
                new Genre(null, book.getGenreName()));
    }

    public RepositoryItemWriter<Book> writer() {
        return new RepositoryItemWriterBuilder<Book>()
                .repository(bookRepository)
                .methodName("insert")
                .build();
    }

    // Шаги, которые не требуют пакетной обработки - Тасклеты
    public Step cleanUpStep() {
        return stepBuilderFactory.get("cleanUpStep")
                .tasklet(cleanUpTasklet())
                .build();
    }

    public MethodInvokingTaskletAdapter cleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }
}
