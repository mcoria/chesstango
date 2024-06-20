package net.chesstango.board.iterators.bysquare;

import net.chesstango.board.Square;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */

public class BishopPositionTest {


    @Test
    public void testPares() {
        long posicionesPares = 0;

        int totalPares = 0;
        for (int i = 0; i < 64; i += 2) {
            //System.out.println(Square.getSquareByIdx(i));
            posicionesPares |= Square.getSquareByIdx(i).getBitPosition();

            if (i % 8 == 6) {
                i++;
            } else if (i % 8 == 7) {
                i--;
            }
            totalPares++;
        }
        assertTrue((posicionesPares & Square.a1.getBitPosition()) != 0);
        assertTrue((posicionesPares & Square.b1.getBitPosition()) == 0);
        assertTrue((posicionesPares & Square.a2.getBitPosition()) == 0);
        assertTrue((posicionesPares & Square.b2.getBitPosition()) != 0);
        assertEquals(32, totalPares);


        HexFormat hexFormat = HexFormat.of().withUpperCase();
        //System.out.printf("0x%sL,%n", hexFormat.formatHex(longToByte(posicionesPares)));


        assertEquals(0xAA55AA55AA55AA55L, posicionesPares);
    }

    @Test
    public void testImpares() {
        long posicionesImpares = 0;

        int totalPares = 0;
        for (int i = 1; i < 64; i += 2) {
            //System.out.println(Square.getSquareByIdx(i));
            posicionesImpares |= Square.getSquareByIdx(i).getBitPosition();

            if (i % 8 == 6) {
                i++;
            } else if (i % 8 == 7) {
                i--;
            }
            totalPares++;
        }
        assertTrue((posicionesImpares & Square.a1.getBitPosition()) == 0);
        assertTrue((posicionesImpares & Square.b1.getBitPosition()) != 0);
        assertTrue((posicionesImpares & Square.a2.getBitPosition()) != 0);
        assertTrue((posicionesImpares & Square.b2.getBitPosition()) == 0);
        assertEquals(32, totalPares);


        HexFormat hexFormat = HexFormat.of().withUpperCase();
        //System.out.printf("0x%sL,%n", hexFormat.formatHex(longToByte(posicionesImpares)));


        assertEquals(0x55AA55AA55AA55AAL, posicionesImpares);
    }


    private byte[] longToByte(long lng) {
        return new byte[]{
                (byte) (lng >> 56),
                (byte) (lng >> 48),
                (byte) (lng >> 40),
                (byte) (lng >> 32),
                (byte) (lng >> 24),
                (byte) (lng >> 16),
                (byte) (lng >> 8),
                (byte) lng
        };
    }

}
