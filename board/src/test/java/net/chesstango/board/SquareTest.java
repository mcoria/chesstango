package net.chesstango.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class SquareTest {

    @Test
    public void testToString() {
        Square square = Square.a1;
        assertEquals("a1", square.toString());
    }

    @Test
    public void testGetSquare() {
        assertEquals(Square.a1, Square.of(0, 0));
        assertEquals(Square.b1, Square.of(1, 0));
        assertEquals(Square.c1, Square.of(2, 0));
        assertEquals(Square.d1, Square.of(3, 0));
        assertEquals(Square.e1, Square.of(4, 0));
        assertEquals(Square.f1, Square.of(5, 0));
        assertEquals(Square.g1, Square.of(6, 0));
        assertEquals(Square.h1, Square.of(7, 0));

        assertEquals(Square.a2, Square.of(0, 1));
        assertEquals(Square.b2, Square.of(1, 1));
        assertEquals(Square.c2, Square.of(2, 1));
        assertEquals(Square.d2, Square.of(3, 1));
        assertEquals(Square.e2, Square.of(4, 1));
        assertEquals(Square.f2, Square.of(5, 1));
        assertEquals(Square.g2, Square.of(6, 1));
        assertEquals(Square.h2, Square.of(7, 1));

        assertEquals(Square.a3, Square.of(0, 2));
        assertEquals(Square.b3, Square.of(1, 2));
        assertEquals(Square.c3, Square.of(2, 2));
        assertEquals(Square.d3, Square.of(3, 2));
        assertEquals(Square.e3, Square.of(4, 2));
        assertEquals(Square.f3, Square.of(5, 2));
        assertEquals(Square.g3, Square.of(6, 2));
        assertEquals(Square.h3, Square.of(7, 2));

        assertEquals(Square.a4, Square.of(0, 3));
        assertEquals(Square.b4, Square.of(1, 3));
        assertEquals(Square.c4, Square.of(2, 3));
        assertEquals(Square.d4, Square.of(3, 3));
        assertEquals(Square.e4, Square.of(4, 3));
        assertEquals(Square.f4, Square.of(5, 3));
        assertEquals(Square.g4, Square.of(6, 3));
        assertEquals(Square.h4, Square.of(7, 3));

        assertEquals(Square.a5, Square.of(0, 4));
        assertEquals(Square.b5, Square.of(1, 4));
        assertEquals(Square.c5, Square.of(2, 4));
        assertEquals(Square.d5, Square.of(3, 4));
        assertEquals(Square.e5, Square.of(4, 4));
        assertEquals(Square.f5, Square.of(5, 4));
        assertEquals(Square.g5, Square.of(6, 4));
        assertEquals(Square.h5, Square.of(7, 4));

        assertEquals(Square.a6, Square.of(0, 5));
        assertEquals(Square.b6, Square.of(1, 5));
        assertEquals(Square.c6, Square.of(2, 5));
        assertEquals(Square.d6, Square.of(3, 5));
        assertEquals(Square.e6, Square.of(4, 5));
        assertEquals(Square.f6, Square.of(5, 5));
        assertEquals(Square.g6, Square.of(6, 5));
        assertEquals(Square.h6, Square.of(7, 5));

        assertEquals(Square.a7, Square.of(0, 6));
        assertEquals(Square.b7, Square.of(1, 6));
        assertEquals(Square.c7, Square.of(2, 6));
        assertEquals(Square.d7, Square.of(3, 6));
        assertEquals(Square.e7, Square.of(4, 6));
        assertEquals(Square.f7, Square.of(5, 6));
        assertEquals(Square.g7, Square.of(6, 6));
        assertEquals(Square.h7, Square.of(7, 6));

        assertEquals(Square.a8, Square.of(0, 7));
        assertEquals(Square.b8, Square.of(1, 7));
        assertEquals(Square.c8, Square.of(2, 7));
        assertEquals(Square.d8, Square.of(3, 7));
        assertEquals(Square.e8, Square.of(4, 7));
        assertEquals(Square.f8, Square.of(5, 7));
        assertEquals(Square.g8, Square.of(6, 7));
        assertEquals(Square.h8, Square.of(7, 7));
    }

    @Test
    public void testPosicion() {
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000001L, Square.a1.bitPosition());
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000010L, Square.b1.bitPosition());

        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000001_00000000L, Square.a2.bitPosition());
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000010_00000000L, Square.b2.bitPosition());

        assertEquals(0b01000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L, Square.g8.bitPosition());
        assertEquals(0b10000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L, Square.h8.bitPosition());
    }

    @Test
    public void testPosicion01() {
        long posiciones = 0;
        Square[] values = Square.values();
        for (int i = 0; i < values.length; i++) {
            posiciones &= values[i].bitPosition();
        }

        assertEquals(0, posiciones);
    }

    @Test
    public void testMirror() {
        assertEquals(Square.a8, Square.a1.mirror());
        assertEquals(Square.h1, Square.h8.mirror());
    }

    @Test
    public void testIdx() {
        for (Square square : Square.values()) {
            assertEquals(square, Square.squareByIdx(square.idx()));
        }
    }

    @Test
    public void testNorth() {
        Square[] northSquares = {Square.a8, Square.b8, Square.c8, Square.d8, Square.e8, Square.f8, Square.g8, Square.h8};
        long northLimit = 0;
        for (int i = 0; i < 8; i++) {
            northLimit |= northSquares[i].bitPosition();
        }

        //System.out.printf("0x%sL\n", Long.toHexString(northLimit).toUpperCase());
        assertEquals(0xFF00000000000000L, northLimit);
    }

    @Test
    public void testSouth() {
        Square[] northSquares = {Square.a1, Square.b1, Square.c1, Square.d1, Square.e1, Square.f1, Square.g1, Square.h1};
        long northLimit = 0;
        for (int i = 0; i < 8; i++) {
            northLimit |= northSquares[i].bitPosition();
        }

        //System.out.printf("0x%sL\n", Long.toHexString(northLimit).toUpperCase());
        assertEquals(0x00000000000000FFL, northLimit);
    }

    @Test
    public void testWest() {
        Square[] northSquares = {Square.a1, Square.a2, Square.a3, Square.a4, Square.a5, Square.a6, Square.a7, Square.a8};
        long northLimit = 0;
        for (int i = 0; i < 8; i++) {
            northLimit |= northSquares[i].bitPosition();
        }

        //System.out.printf("0x%sL\n", Long.toHexString(northLimit).toUpperCase());
        assertEquals(0x101010101010101L, northLimit);
    }

    @Test
    public void testEast() {
        Square[] northSquares = {Square.h1, Square.h2, Square.h3, Square.h4, Square.h5, Square.h6, Square.h7, Square.h8};
        long northLimit = 0;
        for (int i = 0; i < 8; i++) {
            northLimit |= northSquares[i].bitPosition();
        }

        //System.out.printf("0x%sL\n", Long.toHexString(northLimit).toUpperCase());
        assertEquals(0x8080808080808080L, northLimit);
    }


    @Test
    public void testWhiteCastlingQueen() {
        Square[] squares = {Square.b1, Square.c1, Square.d1};
        long squaresLgn = 0;
        for (Square square : squares) {
            squaresLgn |= square.bitPosition();
        }

        //System.out.printf("0x%sL\n", Long.toHexString(squaresLgn).toUpperCase());
        assertEquals(0x000000000000000EL, squaresLgn);
    }

    @Test
    public void testWhiteCastlingKing() {
        Square[] squares = {Square.f1, Square.g1};
        long squaresLgn = 0;
        for (Square square : squares) {
            squaresLgn |= square.bitPosition();
        }

        //System.out.printf("0x%sL\n", Long.toHexString(squaresLgn).toUpperCase());
        assertEquals(0x0000000000000060L, squaresLgn);
    }

    @Test
    public void test_c1() {
        Square[] squares = {Square.c1};
        long squaresLgn = 0;
        for (Square square : squares) {
            squaresLgn |= square.bitPosition();
        }

        //System.out.printf("0x%sL\n", Long.toHexString(squaresLgn).toUpperCase());
        assertEquals(0x0000000000000004L, squaresLgn);
    }

    @Test
    public void test_d1() {
        Square[] squares = {Square.d1};
        long squaresLgn = 0;
        for (Square square : squares) {
            squaresLgn |= square.bitPosition();
        }

        //System.out.printf("0x%sL\n", Long.toHexString(squaresLgn).toUpperCase());
        assertEquals(0x0000000000000008L, squaresLgn);
    }

    @Test
    public void test_e1() {
        Square[] squares = {Square.e1};
        long squaresLgn = 0;
        for (Square square : squares) {
            squaresLgn |= square.bitPosition();
        }

        //System.out.printf("0x%sL\n", Long.toHexString(squaresLgn).toUpperCase());
        assertEquals(0x10L, squaresLgn);
    }

    @Test
    public void test_f1() {
        Square[] squares = {Square.f1};
        long squaresLgn = 0;
        for (Square square : squares) {
            squaresLgn |= square.bitPosition();
        }

        //System.out.printf("0x%sL\n", Long.toHexString(squaresLgn).toUpperCase());
        assertEquals(0x20L, squaresLgn);
    }

    @Test
    public void test_g1() {
        Square[] squares = {Square.g1};
        long squaresLgn = 0;
        for (Square square : squares) {
            squaresLgn |= square.bitPosition();
        }

        //System.out.printf("0x%sL\n", Long.toHexString(squaresLgn).toUpperCase());
        assertEquals(0x40L, squaresLgn);
    }
}
