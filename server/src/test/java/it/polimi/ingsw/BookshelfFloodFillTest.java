package it.polimi.ingsw;

import it.polimi.ingsw.mock.DynamicTestBookshelf;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.util.BookshelfFloodFill;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for flood fill algorithm on bookshelf")
public class BookshelfFloodFillTest {

    static Stream<Arguments> bookshelfProvider() {
        return Stream.of(
           Arguments.of(0, Named.of("Group of size 0", new DynamicTestBookshelf(new int[][] {{-1}}))),
           Arguments.of(1, Named.of("Group of size 1", new DynamicTestBookshelf(new int[][] {{1}}))),
           Arguments.of(2, Named.of("Group of size 2", new DynamicTestBookshelf(new int[][] {{1,1}}))),
           Arguments.of(3, Named.of("Group of size 3", new DynamicTestBookshelf(new int[][] {{1,1,1}}))),
           Arguments.of(4, Named.of("Group of size 4", new DynamicTestBookshelf(new int[][] {{1,1,1,1}}))),
           Arguments.of(5, Named.of("Group of size 5", new DynamicTestBookshelf(new int[][] {{1,1,1,1,1}}))),
           Arguments.of(6, Named.of("Group of size 6", new DynamicTestBookshelf(new int[][] {{1,1,1,1,1,1}}))),
           Arguments.of(7, Named.of("Group of size 6+", new DynamicTestBookshelf(new int[][] {{1,1,1,1,1,1,1}})))
        );
    }

    @ParameterizedTest
    @DisplayName("When evaluating sizes")
    @MethodSource("bookshelfProvider")
    void correctGroupSize(int expectedSize, IBookshelf bookshelf) {
        List<Integer> sizes = BookshelfFloodFill.getTileGroupsSizes(bookshelf);
        if(expectedSize == 0) assertEquals(expectedSize, sizes.size());
        else assertAll(
                () -> assertEquals(1, sizes.size()),
                () -> assertEquals(expectedSize, sizes.get(0))
        );
    }
}
