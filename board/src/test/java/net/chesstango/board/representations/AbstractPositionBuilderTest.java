package net.chesstango.board.representations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class AbstractPositionBuilderTest {
    AbstractPositionBuilder<?> builder;

    @BeforeEach
    public void setup() {
        builder = new AbstractPositionBuilder<>() {
            @Override
            public Object getPositionRepresentation() {
                return null;
            }
        };
    }

    @Test
    public void test01() {
        builder.withEnPassantSquare(0, 0);
        assertEquals(1L << 0, builder.enPassantSquare);

        builder.withEnPassantSquare(1, 0);
        assertEquals(1L << 1, builder.enPassantSquare);

        builder.withEnPassantSquare(7, 0);
        assertEquals(1L << 7, builder.enPassantSquare);

        builder.withEnPassantSquare(0, 1);
        assertEquals(1L << 8, builder.enPassantSquare);

        builder.withEnPassantSquare(1, 2);
        assertEquals(1L << (1 + 2 * 8), builder.enPassantSquare);

        builder.withEnPassantSquare(7, 7);
        assertEquals(1L << 63, builder.enPassantSquare);

    }
}
