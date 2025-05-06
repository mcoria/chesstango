package net.chesstango.board.iterators;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.CardinalSquareIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

/**
 * @author Mauricio Coria
 *
 */
public class CardinalTest {

    @Test
    public void testNorte(){
        Cardinal norte = Cardinal.Norte;

        Square a1 = Square.a1;

        long a1_posiciones = norte.getSquaresInDirection(a1);

        Assertions.assertTrue( (a1_posiciones & Square.a2.bitPosition()) != 0 );
        Assertions.assertTrue( (a1_posiciones & Square.a3.bitPosition()) != 0 );
        Assertions.assertTrue( (a1_posiciones & Square.a4.bitPosition()) != 0 );
        Assertions.assertTrue( (a1_posiciones & Square.a5.bitPosition()) != 0 );
        Assertions.assertTrue( (a1_posiciones & Square.a6.bitPosition()) != 0 );
        Assertions.assertTrue( (a1_posiciones & Square.a7.bitPosition()) != 0 );
        Assertions.assertTrue( (a1_posiciones & Square.a8.bitPosition()) != 0 );

        Assertions.assertFalse( (a1_posiciones & Square.b2.bitPosition()) != 0 );
        Assertions.assertFalse( (a1_posiciones & Square.b3.bitPosition()) != 0 );
        Assertions.assertFalse( (a1_posiciones & Square.b4.bitPosition()) != 0 );
        Assertions.assertFalse( (a1_posiciones & Square.b5.bitPosition()) != 0 );
        Assertions.assertFalse( (a1_posiciones & Square.b6.bitPosition()) != 0 );
        Assertions.assertFalse( (a1_posiciones & Square.b7.bitPosition()) != 0 );
        Assertions.assertFalse( (a1_posiciones & Square.b8.bitPosition()) != 0 );
    }


    //@Test
    public void testReachedSquares() {
        long posiciones[] = new long[64];
        for (Square squareOrigen : Square.values()) {
            CardinalSquareIterator cardinalIterator = new CardinalSquareIterator(squareOrigen, Cardinal.NorteOeste);
            while (cardinalIterator.hasNext()) {
                Square posicion = cardinalIterator.next();
                if (!squareOrigen.equals(posicion)) {
                    posiciones[squareOrigen.idx()] |= posicion.bitPosition();
                }
            }
        }

        for (long posicion : posiciones) {
            HexFormat hexFormat = HexFormat.of().withUpperCase();
            System.out.println(String.format("0x%sL,", hexFormat.formatHex(longToByte(posicion))));
            //System.out.println(Long.toHexString(arraySaltos[idx]));
        }

    }

    private byte[] longToByte(long lng) {
        byte[] b = new byte[]{
                (byte) (lng >> 56),
                (byte) (lng >> 48),
                (byte) (lng >> 40),
                (byte) (lng >> 32),
                (byte) (lng >> 24),
                (byte) (lng >> 16),
                (byte) (lng >> 8),
                (byte) lng
        };
        return b;
    }
}
