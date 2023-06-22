package net.chesstango.board.iterators.bysquare;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.bypiece.KingSquareIterator;
import net.chesstango.board.iterators.bysquare.bypiece.KnightSquareIterator;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;


/**
 * @author Mauricio Coria
 */
public class JumpSquareIteratorTest {

    @Test
    public void printKnightJumpPositions(){
        long[] arraySaltos = new long[64];
        for (int idx = 0; idx < 64; idx++) {
            Square square = Square.getSquareByIdx(idx);
            JumpSquareIterator iterator = new JumpSquareIterator(square, KnightSquareIterator.KNIGHT_JUMPS_OFFSETS);
            long posicionesSalto = 0;
            while (iterator.hasNext()) {
                Square salto = iterator.next();

                posicionesSalto |= salto.getBitPosition();
            }
            arraySaltos[idx] = posicionesSalto;
        }
        for (int idx = 0; idx < 64; idx++) {
            HexFormat hexFormat = HexFormat.of().withUpperCase();
            System.out.println(String.format("0x%sL,", hexFormat.formatHex(longToByte(arraySaltos[idx]))));
            //System.out.println(Long.toHexString(arraySaltos[idx]));
        }
    }

    //@Test
    public void printKingJumpPositions() {
        long[] arraySaltos = new long[64];
        for (int idx = 0; idx < 64; idx++) {
            Square square = Square.getSquareByIdx(idx);
            JumpSquareIterator iterator = new JumpSquareIterator(square, KingSquareIterator.KING_JUMP_OFFSETS);
            long posicionesSalto = 0;
            while (iterator.hasNext()) {
                Square salto = iterator.next();
                posicionesSalto |= salto.getBitPosition();
            }
            arraySaltos[idx] = posicionesSalto;
        }


        for (int idx = 0; idx < 64; idx++) {
            HexFormat hexFormat = HexFormat.of().withUpperCase();
            System.out.println(String.format("0x%sL,", hexFormat.formatHex(longToByte(arraySaltos[idx]))));
            //System.out.println(Long.toHexString(arraySaltos[idx]));
        }
    }

    private final int[][] casillerosPawnWhite = {
            {-1, -1},
            {1, -1}
    };

    //@Test
    public void printSaltosPawnWhitePosicionesLong() {
        long[] arraySaltos = new long[64];
        for (int idx = 0; idx < 64; idx++) {
            Square square = Square.getSquareByIdx(idx);
            JumpSquareIterator iterator = new JumpSquareIterator(square, casillerosPawnWhite);
            long posicionesSalto = 0;
            while (iterator.hasNext()) {
                Square salto = iterator.next();

                posicionesSalto |= salto.getBitPosition();
            }
            arraySaltos[idx] = posicionesSalto;
        }

        for (int idx = 0; idx < 64; idx++) {
            System.out.println(arraySaltos[idx] + "L,");
        }

    }

    private final int[][] casillerosPawnBlack = {
            {-1, 1},
            {1, 1}
    };

    //@Test
    public void printSaltosPawnBlackPosicionesLong() {
        long[] arraySaltos = new long[64];
        for (int idx = 0; idx < 64; idx++) {
            Square square = Square.getSquareByIdx(idx);
            JumpSquareIterator iterator = new JumpSquareIterator(square, casillerosPawnBlack);
            long posicionesSalto = 0;
            while (iterator.hasNext()) {
                Square salto = iterator.next();

                posicionesSalto |= salto.getBitPosition();
            }
            arraySaltos[idx] = posicionesSalto;
        }

        for (int idx = 0; idx < 64; idx++) {
            System.out.println(arraySaltos[idx] + "L,");
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
