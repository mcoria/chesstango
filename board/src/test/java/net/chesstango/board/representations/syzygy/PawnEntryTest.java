package net.chesstango.board.representations.syzygy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class PawnEntryTest {
    public static final String PATH = "C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5";

    private Syzygy syzygy;
    private PawnEntry pawnEntry;

    @BeforeEach
    public void setUp() throws Exception {
        syzygy = new Syzygy();
        pawnEntry = new PawnEntry(syzygy);
    }

    /**
     * Test for the "KPvKP" tableType: tableType with PAWNs
     */
    @Test
    public void test_init_tb_KPvKP() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KPvKP");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        pawnEntry = syzygy.pawnEntry[0];
        assertEquals(0x8E59ED7027C162EAL, pawnEntry.key);
        assertEquals(4, pawnEntry.num);
        assertTrue(pawnEntry.symmetric);
        assertEquals(1, pawnEntry.pawns[0]);
        assertEquals(1, pawnEntry.pawns[1]);


        /**
         * HashEntry assertions
         */
        Syzygy.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[2277];
        assertEquals(0x8E59ED7027C162EAL, tbHash.key);
        assertSame(tbHash.ptr, pawnEntry);

        /**
         * WDL table assertions
         */
        assertNotNull(pawnEntry.wdl);
        PawnSymmetricWdl wdl = (PawnSymmetricWdl) pawnEntry.wdl;

        EncInfo ei0 = wdl.ei[0];
        assertArrayEquals(new int[]{177754, 3782, 1, 62, 0, 0, 0}, ei0.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei0.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei0.norm);

        PairsData ei0_precomp = ei0.precomp;
        assertEquals(18828, ei0_precomp.indexTable.ptr);
        assertEquals(19620, ei0_precomp.sizeTable.ptr);
        assertEquals(26496, ei0_precomp.data.ptr);
        assertEquals(28, ei0_precomp.offset.ptr);
        assertEquals(64, ei0_precomp.symPat.ptr);

        assertEquals(6, ei0_precomp.blockSize);
        assertEquals(15, ei0_precomp.idxBits);
        assertEquals(6, ei0_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0.precomp.constValue);
        assertArrayEquals(new long[]{
                0xFC00000000000000L,
                0xEC00000000000000L,
                0xB800000000000000L,
                0x8700000000000000L,
                0x5A80000000000000L,
                0x32A0000000000000L,
                0x380000000000000L,
                0xD0000000000000L,
                0x28000000000000L,
                0x4000000000000L, 0
        }, ei0_precomp.base);
        assertEquals(1581, ei0_precomp.symLen.length);


        EncInfo ei1 = wdl.ei[1];
        assertArrayEquals(new int[]{177754, 62, 1, 2914, 0, 0, 0}, ei1.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei1.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei1.norm);

        PairsData ei1_precomp = ei1.precomp;
        assertEquals(19026, ei1_precomp.indexTable.ptr);
        assertEquals(21262, ei1_precomp.sizeTable.ptr);
        assertEquals(79040, ei1_precomp.data.ptr);
        assertEquals(4806, ei1_precomp.offset.ptr);
        assertEquals(4842, ei1_precomp.symPat.ptr);

        assertEquals(6, ei1_precomp.blockSize);
        assertEquals(15, ei1_precomp.idxBits);
        assertEquals(6, ei1_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_precomp.constValue);
        assertArrayEquals(new long[]{
                0xFC00000000000000L,
                0xF000000000000000L,
                0xB200000000000000L,
                0x8880000000000000L,
                0x5A40000000000000L,
                0x31A0000000000000L,
                0x370000000000000L,
                0xF8000000000000L,
                0x40000000000000L,
                0x10000000000000L,
                0}, ei1_precomp.base);
        assertEquals(1587, ei1_precomp.symLen.length);


        EncInfo ei2 = wdl.ei[2];
        assertArrayEquals(new int[]{2914, 62, 1, 17484, 0, 0, 0}, ei2.factor);
        assertArrayEquals(new byte[]{1, 9, 14, 6, 0, 0, 0}, ei2.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei2.norm);

        PairsData ei2_precomp = ei2.precomp;
        assertEquals(19224, ei2_precomp.indexTable.ptr);
        assertEquals(23008, ei2_precomp.sizeTable.ptr);
        assertEquals(134912, ei2_precomp.data.ptr);
        assertEquals(9600, ei2_precomp.offset.ptr);
        assertEquals(9636, ei2_precomp.symPat.ptr);

        assertEquals(6, ei2_precomp.blockSize);
        assertEquals(15, ei2_precomp.idxBits);
        assertEquals(7, ei2_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_precomp.constValue);
        assertArrayEquals(new long[]{
                0xF000000000000000L,
                0xB400000000000000L,
                0x7F80000000000000L,
                0x5500000000000000L,
                0x2D00000000000000L,
                0x860000000000000L,
                0x98000000000000L,
                0x24000000000000L,
                0x8000000000000L,
                0}, ei2_precomp.base);
        assertEquals(1558, ei2_precomp.symLen.length);

    }

    /**
     * Test for the "KPvK" tableType: tableType with PAWNs
     */
    @Test
    public void test_init_tb_KPvK() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KPvK");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        PawnEntry baseEntry = syzygy.pawnEntry[0];
        assertEquals(0xec0ade190c0f6003L, baseEntry.key);
        assertEquals(3, baseEntry.num);
        assertFalse(baseEntry.symmetric);
        assertEquals(1, baseEntry.pawns[0]);
        assertEquals(0, baseEntry.pawns[1]);

        Syzygy.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[3776];
        assertEquals(0xec0ade190c0f6003L, tbHash.key);
        assertSame(tbHash.ptr, baseEntry);

        tbHash = syzygy.tbHash[2596];
        assertEquals(0xa24f0f571bb202e7L, tbHash.key);
        assertSame(tbHash.ptr, baseEntry);
    }
}
