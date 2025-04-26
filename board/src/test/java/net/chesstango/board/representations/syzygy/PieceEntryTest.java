package net.chesstango.board.representations.syzygy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class PieceEntryTest {

    private Syzygy syzygy;
    private PieceEntry pieceEntry;

    @BeforeEach
    public void setUp() throws Exception {
        syzygy = new Syzygy();
        pieceEntry = new PieceEntry(syzygy);
    }

    @Test
    public void test_init_table_KQvK() {
        syzygy.setPath("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");
        pieceEntry.init_tb("KQvK");

        assertEquals("KQvK", pieceEntry.tableName);
        assertEquals(0xa3ec1abc71e90863L, pieceEntry.key);
        assertEquals(3, pieceEntry.num);
        assertFalse(pieceEntry.symmetric);
        assertFalse(pieceEntry.kk_enc);

        assertFalse(pieceEntry.dtmLossOnly);

        assertEquals(662, pieceEntry.dtzMap.ptr);
        assertArrayEquals(new short[]{0, 0, 0, 0}, pieceEntry.dtzMapIdx);
        assertEquals(0, pieceEntry.dtzFlags);


        /**
         * WDL table assertions
         */
        assertNotNull(pieceEntry.wdl);
        PieceAsymmetricWdl wdl = (PieceAsymmetricWdl) pieceEntry.wdl;

        EncInfo ei_wtm = wdl.ei_wtm;
        assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0}, ei_wtm.factor);
        assertArrayEquals(new byte[]{14, 6, 5, 0, 0, 0, 0}, ei_wtm.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 0, 0, 0, 0}, ei_wtm.norm);
        PairsData ei_wtm_precomp = ei_wtm.precomp;
        assertEquals(112, ei_wtm_precomp.indexTable.ptr);
        assertEquals(118, ei_wtm_precomp.sizeTable.ptr);
        assertEquals(128, ei_wtm_precomp.data.ptr);
        assertNull(ei_wtm_precomp.offset);
        assertNull(ei_wtm_precomp.symLen);
        assertNull(ei_wtm_precomp.symPat);
        assertNull(ei_wtm_precomp.base);
        assertEquals(0, ei_wtm_precomp.blockSize);
        assertEquals(0, ei_wtm_precomp.idxBits);
        assertEquals(0, ei_wtm_precomp.minLen);
        assertArrayEquals(new byte[]{4, 0}, ei_wtm.precomp.constValue);


        EncInfo ei_btm = wdl.ei_btm;
        assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0}, ei_btm.factor);
        assertArrayEquals(new byte[]{14, 6, 5, 0, 0, 0, 0}, ei_btm.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 0, 0, 0, 0}, ei_btm.norm);
        PairsData ei_btm_precomp = ei_btm.precomp;
        assertEquals(112, ei_btm_precomp.indexTable.ptr);
        assertEquals(118, ei_btm_precomp.sizeTable.ptr);
        assertEquals(128, ei_btm_precomp.data.ptr);
        assertEquals(20, ei_btm_precomp.offset.ptr);
        assertArrayEquals(new byte[]{63, 54, 11, 127, 17, 8, 15, 1, 3, 6, 31, -65, 123, 7, 0, 1,
                47, 59, 7, 1, -1, 0, 61, 5}, ei_btm_precomp.symLen);
        assertEquals(40, ei_btm_precomp.symPat.ptr);
        assertEquals(6, ei_btm_precomp.blockSize);
        assertEquals(15, ei_btm_precomp.idxBits);
        assertEquals(1, ei_btm_precomp.minLen);
        assertArrayEquals(new byte[]{0, 0}, ei_btm_precomp.constValue);
        assertArrayEquals(new long[]{0x8000000000000000L, 0x8000000000000000L, 0x8000000000000000L,
                0x6000000000000000L, 0x2000000000000000L, 0x400000000000000L, 0x200000000000000L,
                0x0L}, ei_btm_precomp.base);


        /**
         * DTZ table assertions
         */
        assertNotNull(pieceEntry.dtz);
        PieceAsymmetricDtz dtz = (PieceAsymmetricDtz) pieceEntry.dtz;

        EncInfo ei_dtz = dtz.ei_dtz;
        assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0}, ei_dtz.factor);
        assertArrayEquals(new byte[]{14, 6, 5, 0, 0, 0, 0}, ei_dtz.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 0, 0, 0, 0}, ei_dtz.norm);
        PairsData ei_dtz_precomp = ei_dtz.precomp;
        assertEquals(662, ei_dtz_precomp.indexTable.ptr);
        assertEquals(668, ei_dtz_precomp.sizeTable.ptr);
        assertEquals(704, ei_dtz_precomp.data.ptr);
        assertEquals(8, ei_dtz_precomp.offset.ptr);
        assertEquals(209, ei_dtz_precomp.symLen.length);
        assertEquals(34, ei_dtz_precomp.symPat.ptr);
        assertEquals(9, ei_dtz_precomp.blockSize);
        assertEquals(15, ei_dtz_precomp.idxBits);
        assertEquals(6, ei_dtz_precomp.minLen);
        assertArrayEquals(new byte[]{4, 0}, ei_wtm.precomp.constValue);
        assertArrayEquals(new long[]{0xAC00000000000000L, 0x6600000000000000L, 0x2C00000000000000L, 0x280000000000000L,
                0x40000000000000L,
                0x0L}, ei_dtz_precomp.base);
    }

    @Test
    @Disabled
    public void test_init_table_KQvKR() {
        syzygy.setPath("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");
        pieceEntry.init_tb("KQvKR");

        assertEquals("KQvKR", pieceEntry.tableName);
        assertEquals(4, pieceEntry.num);
        assertFalse(pieceEntry.symmetric);
        assertFalse(pieceEntry.kk_enc);
    }
}
