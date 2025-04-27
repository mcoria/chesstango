package net.chesstango.board.representations.syzygy;

import net.chesstango.board.position.Position;
import net.chesstango.board.representations.fen.FEN;
import org.junit.jupiter.api.Test;

import static net.chesstango.board.representations.syzygy.BaseEntry.calc_key_from_pcs;
import static net.chesstango.board.representations.syzygy.Syzygy.calcKey;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class SyzygyConstantsTest {

    @Test
    public void test_tableName_to_pcs() {
        int[] pcs = tableName_to_pcs("KQvK");
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0}, pcs);
    }

    @Test
    public void test_calc_key_from_pcs() {
        int[] pcs = tableName_to_pcs("KQvK");
        long key = calc_key_from_pcs(pcs, false);
        long key2 = calc_key_from_pcs(pcs, true);
        assertEquals(0xa3ec1abc71e90863L, key);
        assertEquals(0xd6e4e47d24962951L, key2);
    }

    @Test
    public void test_calcKey() {
        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 w - - 0 1");

        Position chessPosition = fen.toChessPosition();

        BitPosition bitPosition = BitPosition.from(chessPosition);

        assertEquals(0xa3ec1abc71e90863L, calcKey(bitPosition));

        assertEquals(2622, 0xa3ec1abc71e90863L >>> (64 - TB_HASHBITS));
    }

    @Test
    public void test_test_tb() {
        assertFalse(test_tb("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5", "KQvK", ".rtbm"));
    }

    @Test
    public void test_prt_str() {
        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 w - - 0 1");

        Position chessPosition = fen.toChessPosition();

        BitPosition bitPosition = BitPosition.from(chessPosition);

        assertEquals("KQvK", prt_str(bitPosition, false));
    }

    @Test
    public void test_byte_bitshift() {
        byte aPositiveByte = (byte) 0b0010_0001;
        byte aNegativeByte = (byte) 0b1010_0001;

        // Left shift
        assertEquals((byte) 0b0100_0010, (byte) ((aPositiveByte & 0xFF) << 1));
        assertEquals((byte) 0b0001_0000, (byte) ((aPositiveByte & 0xFF) << 4));

        assertEquals((byte) 0b0100_0010, (byte) ((aNegativeByte & 0xFF) << 1));
        assertEquals((byte) 0b0001_0000, (byte) ((aNegativeByte & 0xFF) << 4));

        // Right shift
        assertEquals((byte) 0b0001_0000, (byte) ((aPositiveByte & 0xFF) >>> 1));
        assertEquals((byte) 0b0000_0010, (byte) ((aPositiveByte & 0xFF) >>> 4));

        assertEquals((byte) 0b0101_0000, (byte) ((aNegativeByte & 0xFF) >>> 1));
        assertEquals((byte) 0b0000_1010, (byte) ((aNegativeByte & 0xFF) >>> 4));
    }
}
