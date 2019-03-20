package com.baeldung.spliteratorAPI;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class ExecutorUnitTest {
    Article article;
    Stream<Author> stream;
    Spliterator<Author> spliterator;
    Spliterator<Article> split1;
    Spliterator<Article> split2;

    @Before
    public void init() {
        List<Author> listOfAuthors = Arrays.asList(
                new Author("Ahmad", 0), // related to an existing article
                new Author("Eugen", 0),
                new Author("Alice", 1), // related to an non-existing article
                new Author("Alice", 1),
                new Author("Mike", 0),
                new Author("Alice", 1),
                new Author("Mike", 0),
                new Author("Alice", 1),
                new Author("Mike", 0),
                new Author("Alice", 1),
                new Author("Mike", 0),
                new Author("Mike", 0),
                new Author("Alice", 1),
                new Author("Mike", 0),
                new Author("Alice", 1),
                new Author("Mike", 0),
                new Author("Alice", 1),
                new Author("Mike", 0),
                new Author("Alice", 1),
                new Author("Mike", 0),
                new Author("Michał", 0),
                new Author("Loredana", 1));
        article = new Article(listOfAuthors, 0);
        stream = article.getListOfAuthors().stream();

        split1 = Executor.generateElements().spliterator();
        split2 = split1.trySplit();

        spliterator = new RelatedAuthorSpliterator(article.getListOfAuthors());
    }


    @Test
    public void givenAStreamOfAuthors_whenProcessedSequentially_countProducesRightOutput() {
        int actual = Executor.countAuthors(stream);
        System.out.println(actual);
        assertThat(actual).isEqualTo(9);
    }

    @Test
    public void givenAStreamOfAuthors_whenProcessedInParallelWithoutCustomSpliterator_countProducesWrongOutput() {
        assertThat(Executor.countAuthors(stream.parallel())).isGreaterThan(9); // 10; Apparently, something has gone wrong – splitting the stream at a random position caused an author to be counted twice.
    }

    @Test
    public void givenAstreamOfAuthors_whenProcessedInParallelWithCustomSpliterator_countProducesRightOutput() {
        Stream<Author> stream2 = StreamSupport.stream(spliterator, true);
        assertThat(Executor.countAuthors(stream2.parallel())).isEqualTo(9);
    }

    @Test
    public void givenSpliterator_whenAppliedToAListOfArticle_thenSplittedInHalf() {
        assertThat(new Task(split1).call()).containsSequence(Executor.generateElements().size() / 2 + "");
        assertThat(new Task(split2).call()).containsSequence(Executor.generateElements().size() / 2 + "");
    }
}
