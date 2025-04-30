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


        EncInfo ei3 = wdl.ei[3];
        assertArrayEquals(new int[]{177754, 3782, 1, 62, 0, 0, 0}, ei3.factor);
        assertArrayEquals(new byte[]{1, 9, 14, 6, 0, 0, 0}, ei3.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei3.norm);

        PairsData ei3_precomp = ei3.precomp;
        assertEquals(19422, ei3_precomp.indexTable.ptr);
        assertEquals(24802, ei3_precomp.sizeTable.ptr);
        assertEquals(192320, ei3_precomp.data.ptr);
        assertEquals(14306, ei3_precomp.offset.ptr);
        assertEquals(14342, ei3_precomp.symPat.ptr);

        assertEquals(6, ei3_precomp.blockSize);
        assertEquals(15, ei3_precomp.idxBits);
        assertEquals(7, ei3_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_precomp.constValue);
        assertArrayEquals(new long[]{
                0xE400000000000000L,
                0xAD00000000000000L,
                0x7F80000000000000L,
                0x5940000000000000L,
                0x2EC0000000000000L,
                0x300000000000000L,
                0xA0000000000000L,
                0x24000000000000L,
                0x4000000000000L,
                0}, ei3_precomp.base);
        assertEquals(1495, ei3_precomp.symLen.length);


        /**
         * DTZ table assertions
         */
        assertNotNull(pawnEntry.dtz);
        PawnDtz dtz = (PawnDtz) pawnEntry.dtz;

        assertEquals(5102, dtz.dtzMap.ptr);
        assertArrayEquals(new short[][]{{1, 8, 16, 17}, {18, 30, 41, 42}, {43, 54, 64, 65}, {66, 76, 85, 86}}, dtz.dtzMapIdx);
        assertArrayEquals(new byte[]{2, 2, 2, 2}, dtz.dtzFlags);

        EncInfo ei0_dtz = dtz.ei[0];
        assertArrayEquals(new int[]{62, 22692, 1, 372, 0, 0, 0}, ei0_dtz.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei0_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei0_dtz.norm);

        PairsData ei0_dtz_precomp = ei0_dtz.precomp;
        assertEquals(5188, ei0_dtz_precomp.indexTable.ptr);
        assertEquals(5308, ei0_dtz_precomp.sizeTable.ptr);
        assertEquals(6080, ei0_dtz_precomp.data.ptr);
        assertEquals(36, ei0_dtz_precomp.offset.ptr);
        assertEquals(68, ei0_dtz_precomp.symPat.ptr);

        assertEquals(7, ei0_dtz_precomp.blockSize);
        assertEquals(18, ei0_dtz_precomp.idxBits);
        assertEquals(2, ei0_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L,
                0xB400000000000000L, 0x7C00000000000000L, 0x4900000000000000L, 0x2800000000000000L,
                0x280000000000000L, 0x60000000000000L, 0x10000000000000L, 0x8000000000000L,
                0}, ei0_dtz_precomp.base);
        assertEquals(328, ei0_dtz_precomp.symLen.length);


        EncInfo ei1_dtz = dtz.ei[1];
        assertArrayEquals(new int[]{177754, 3782, 1, 62, 0, 0, 0}, ei1_dtz.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei1_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei1_dtz.norm);

        PairsData ei1_dtz_precomp = ei1_dtz.precomp;
        assertEquals(5218, ei1_dtz_precomp.indexTable.ptr);
        assertEquals(5456, ei1_dtz_precomp.sizeTable.ptr);
        assertEquals(15296, ei1_dtz_precomp.data.ptr);
        assertEquals(1058, ei1_dtz_precomp.offset.ptr);
        assertEquals(1090, ei1_dtz_precomp.symPat.ptr);

        assertEquals(7, ei1_dtz_precomp.blockSize);
        assertEquals(18, ei1_dtz_precomp.idxBits);
        assertEquals(2, ei1_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L,
                0xC000000000000000L, 0x9000000000000000L, 0x6000000000000000L, 0x3900000000000000L,
                0x15C0000000000000L, 0x180000000000000L, 0x60000000000000L, 0x18000000000000L,
                0}, ei1_dtz_precomp.base);
        assertEquals(500, ei1_dtz_precomp.symLen.length);


        EncInfo ei2_dtz = dtz.ei[2];
        assertArrayEquals(new int[]{177754, 62, 1, 2914, 0, 0, 0}, ei2_dtz.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei2_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei2_dtz.norm);

        PairsData ei2_dtz_precomp = ei2_dtz.precomp;
        assertEquals(5248, ei2_dtz_precomp.indexTable.ptr);
        assertEquals(5678, ei2_dtz_precomp.sizeTable.ptr);
        assertEquals(29248, ei2_dtz_precomp.data.ptr);
        assertEquals(2596, ei2_dtz_precomp.offset.ptr);
        assertEquals(2628, ei2_dtz_precomp.symPat.ptr);

        assertEquals(7, ei2_dtz_precomp.blockSize);
        assertEquals(18, ei2_dtz_precomp.idxBits);
        assertEquals(2, ei2_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L,
                0xBC00000000000000L, 0x8C00000000000000L, 0x5300000000000000L, 0x3380000000000000L,
                0x1000000000000000L, 0xE0000000000000L, 0x50000000000000L, 0x10000000000000L,
                0}, ei2_dtz_precomp.base);
        assertEquals(432, ei2_dtz_precomp.symLen.length);


        EncInfo ei3_dtz = dtz.ei[3];
        assertArrayEquals(new int[]{177754, 62, 1, 2914, 0, 0, 0}, ei3_dtz.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei3_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei3_dtz.norm);

        PairsData ei3_dtz_precomp = ei3_dtz.precomp;
        assertEquals(5278, ei3_dtz_precomp.indexTable.ptr);
        assertEquals(5892, ei3_dtz_precomp.sizeTable.ptr);
        assertEquals(42688, ei3_dtz_precomp.data.ptr);
        assertEquals(3930, ei3_dtz_precomp.offset.ptr);
        assertEquals(3962, ei3_dtz_precomp.symPat.ptr);

        assertEquals(7, ei3_dtz_precomp.blockSize);
        assertEquals(18, ei3_dtz_precomp.idxBits);
        assertEquals(2, ei3_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L,
                0xBC00000000000000L, 0x7C00000000000000L, 0x4B00000000000000L, 0x2D80000000000000L,
                0xA40000000000000L, 0xC0000000000000L, 0x20000000000000L, 0x8000000000000L,
                0}, ei3_dtz_precomp.base);
        assertEquals(380, ei3_dtz_precomp.symLen.length);

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

        pawnEntry = syzygy.pawnEntry[0];
        assertEquals(0XEC0ADE190C0F6003L, pawnEntry.key);
        assertEquals(3, pawnEntry.num);
        assertFalse(pawnEntry.symmetric);
        assertEquals(1, pawnEntry.pawns[0]);
        assertEquals(0, pawnEntry.pawns[1]);

        Syzygy.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[3776];
        assertEquals(0XEC0ADE190C0F6003L, tbHash.key);
        assertSame(tbHash.ptr, pawnEntry);

        tbHash = syzygy.tbHash[2596];
        assertEquals(0XA24F0F571BB202E7L, tbHash.key);
        assertSame(tbHash.ptr, pawnEntry);

        /**
         * WDL table assertions
         */
        assertNotNull(pawnEntry.wdl);
        PawnAsymmetricWdl wdl = (PawnAsymmetricWdl) pawnEntry.wdl;


        /**
         * WHITE WDL table assertions
         */
        EncInfo ei0_wtm = wdl.ei_wtm[0];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei0_wtm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei0_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei0_wtm.norm);

        PairsData ei0_precomp_wtm = ei0_wtm.precomp;
        assertEquals(1670, ei0_precomp_wtm.indexTable.ptr);
        assertEquals(1718, ei0_precomp_wtm.sizeTable.ptr);
        assertEquals(1920, ei0_precomp_wtm.data.ptr);
        assertEquals(24, ei0_precomp_wtm.offset.ptr);
        assertEquals(44, ei0_precomp_wtm.symPat.ptr);

        assertEquals(6, ei0_precomp_wtm.blockSize);
        assertEquals(15, ei0_precomp_wtm.idxBits);
        assertEquals(4, ei0_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0_wtm.precomp.constValue);
        assertArrayEquals(new long[]{
                0xD000000000000000L, 0x8000000000000000L,
                0x1000000000000000L, 0x200000000000000L,
                0
        }, ei0_precomp_wtm.base);
        assertEquals(50, ei0_precomp_wtm.symLen.length);


        EncInfo ei1_wtm = wdl.ei_wtm[1];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei1_wtm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei1_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei1_wtm.norm);

        PairsData ei1_precomp_wtm = ei1_wtm.precomp;
        assertEquals(1682, ei1_precomp_wtm.indexTable.ptr);
        assertEquals(1748, ei1_precomp_wtm.sizeTable.ptr);
        assertEquals(2880, ei1_precomp_wtm.data.ptr);
        assertEquals(374, ei1_precomp_wtm.offset.ptr);
        assertEquals(396, ei1_precomp_wtm.symPat.ptr);

        assertEquals(6, ei1_precomp_wtm.blockSize);
        assertEquals(15, ei1_precomp_wtm.idxBits);
        assertEquals(5, ei1_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_precomp_wtm.constValue);
        assertArrayEquals(new long[]{
                0x9000000000000000L, 0x2C00000000000000L,
                0x200000000000000L, 0x100000000000000L,
                0}, ei1_precomp_wtm.base);
        assertEquals(63, ei1_precomp_wtm.symLen.length);


        EncInfo ei2_wtm = wdl.ei_wtm[2];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei2_wtm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei2_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei2_wtm.norm);

        PairsData ei2_precomp_wtm = ei2_wtm.precomp;
        assertEquals(1694, ei2_precomp_wtm.indexTable.ptr);
        assertEquals(1800, ei2_precomp_wtm.sizeTable.ptr);
        assertEquals(4544, ei2_precomp_wtm.data.ptr);
        assertEquals(804, ei2_precomp_wtm.offset.ptr);
        assertEquals(826, ei2_precomp_wtm.symPat.ptr);

        assertEquals(6, ei2_precomp_wtm.blockSize);
        assertEquals(15, ei2_precomp_wtm.idxBits);
        assertEquals(5, ei2_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_precomp_wtm.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x2400000000000000L,
                0x400000000000000L,
                0x100000000000000L,
                0}, ei2_precomp_wtm.base);
        assertEquals(60, ei2_precomp_wtm.symLen.length);

        EncInfo ei3_wtm = wdl.ei_wtm[3];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei3_wtm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei3_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei3_wtm.norm);

        PairsData ei3_precomp_wtm = ei3_wtm.precomp;
        assertEquals(1706, ei3_precomp_wtm.indexTable.ptr);
        assertEquals(1852, ei3_precomp_wtm.sizeTable.ptr);
        assertEquals(6208, ei3_precomp_wtm.data.ptr);
        assertEquals(1236, ei3_precomp_wtm.offset.ptr);
        assertEquals(1256, ei3_precomp_wtm.symPat.ptr);

        assertEquals(6, ei3_precomp_wtm.blockSize);
        assertEquals(15, ei3_precomp_wtm.idxBits);
        assertEquals(4, ei3_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_precomp_wtm.constValue);
        assertArrayEquals(new long[]{
                0xF000000000000000L,
                0x7000000000000000L,
                0x2800000000000000L,
                0x600000000000000L,
                0}, ei3_precomp_wtm.base);
        assertEquals(58, ei3_precomp_wtm.symLen.length);


        /**
         * BLACK WDL table assertions
         */
        EncInfo ei0_btm = wdl.ei_btm[0];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei0_btm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei0_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei0_btm.norm);

        PairsData ei0_precomp_wbtm = ei0_btm.precomp;
        assertEquals(1676, ei0_precomp_wbtm.indexTable.ptr);
        assertEquals(1732, ei0_precomp_wbtm.sizeTable.ptr);
        assertEquals(2368, ei0_precomp_wbtm.data.ptr);
        assertEquals(196, ei0_precomp_wbtm.offset.ptr);
        assertEquals(218, ei0_precomp_wbtm.symPat.ptr);

        assertEquals(6, ei0_precomp_wbtm.blockSize);
        assertEquals(15, ei0_precomp_wbtm.idxBits);
        assertEquals(4, ei0_precomp_wbtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0_precomp_wbtm.constValue);
        assertArrayEquals(new long[]{
                0xF000000000000000L, 0x7800000000000000L,
                0x800000000000000L, 0x200000000000000L,
                0x100000000000000L,
                0}, ei0_precomp_wbtm.base);
        assertEquals(52, ei0_precomp_wbtm.symLen.length);

        EncInfo ei1_btm = wdl.ei_btm[1];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei1_btm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei1_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei1_btm.norm);

        PairsData ei1_precomp_btm = ei1_btm.precomp;
        assertEquals(1688, ei1_precomp_btm.indexTable.ptr);
        assertEquals(1772, ei1_precomp_btm.sizeTable.ptr);
        assertEquals(3648, ei1_precomp_btm.data.ptr);
        assertEquals(586, ei1_precomp_btm.offset.ptr);
        assertEquals(608, ei1_precomp_btm.symPat.ptr);

        assertEquals(6, ei1_precomp_btm.blockSize);
        assertEquals(15, ei1_precomp_btm.idxBits);
        assertEquals(5, ei1_precomp_btm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_precomp_btm.constValue);
        assertArrayEquals(new long[]{
                0x8800000000000000L, 0x3800000000000000L,
                0x200000000000000L, 0x100000000000000L,
                0}, ei1_precomp_btm.base);
        assertEquals(65, ei1_precomp_btm.symLen.length);


        EncInfo ei2_btm = wdl.ei_btm[2];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei2_btm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei2_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei2_btm.norm);

        PairsData ei2_precomp_btm = ei2_btm.precomp;
        assertEquals(1700, ei2_precomp_btm.indexTable.ptr);
        assertEquals(1824, ei2_precomp_btm.sizeTable.ptr);
        assertEquals(5312, ei2_precomp_btm.data.ptr);
        assertEquals(1006, ei2_precomp_btm.offset.ptr);
        assertEquals(1026, ei2_precomp_btm.symPat.ptr);

        assertEquals(6, ei2_precomp_btm.blockSize);
        assertEquals(15, ei2_precomp_btm.idxBits);
        assertEquals(5, ei2_precomp_btm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_precomp_btm.constValue);
        assertArrayEquals(new long[]{
                0xB000000000000000L,
                0x3400000000000000L,
                0x400000000000000L,
                0}, ei2_precomp_btm.base);
        assertEquals(69, ei2_precomp_btm.symLen.length);

        EncInfo ei3_btm = wdl.ei_btm[3];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei3_btm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei3_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei3_btm.norm);

        PairsData ei3_precomp_btm = ei3_btm.precomp;
        assertEquals(1712, ei3_precomp_btm.indexTable.ptr);
        assertEquals(1874, ei3_precomp_btm.sizeTable.ptr);
        assertEquals(6912, ei3_precomp_btm.data.ptr);
        assertEquals(1432, ei3_precomp_btm.offset.ptr);
        assertEquals(1454, ei3_precomp_btm.symPat.ptr);

        assertEquals(6, ei3_precomp_btm.blockSize);
        assertEquals(15, ei3_precomp_btm.idxBits);
        assertEquals(4, ei3_precomp_btm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_precomp_btm.constValue);
        assertArrayEquals(new long[]{
                0xF000000000000000L, 0x9800000000000000L,
                0x4800000000000000L, 0x600000000000000L,
                0x100000000000000L,
                0}, ei3_precomp_btm.base);
        assertEquals(72, ei3_precomp_btm.symLen.length);

        /**
         * DTZ table assertions
         */
        assertNotNull(pawnEntry.dtz);
        PawnDtz dtz = (PawnDtz) pawnEntry.dtz;

        assertEquals(780, dtz.dtzMap.ptr);
        assertArrayEquals(new short[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}}, dtz.dtzMapIdx);
        assertArrayEquals(new byte[]{0, 0, 0, 0}, dtz.dtzFlags);

        EncInfo ei0_dtz = dtz.ei[0];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei0_dtz.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei0_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei0_dtz.norm);

        PairsData ei0_dtz_precomp = ei0_dtz.precomp;
        assertEquals(780, ei0_dtz_precomp.indexTable.ptr);
        assertEquals(804, ei0_dtz_precomp.sizeTable.ptr);
        assertEquals(832, ei0_dtz_precomp.data.ptr);
        assertEquals(28, ei0_dtz_precomp.offset.ptr);
        assertEquals(44, ei0_dtz_precomp.symPat.ptr);

        assertEquals(8, ei0_dtz_precomp.blockSize);
        assertEquals(15, ei0_dtz_precomp.idxBits);
        assertEquals(2, ei0_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L,
                0xC000000000000000L,
                0x8000000000000000L,
                0x1000000000000000L,
                0}, ei0_dtz_precomp.base);
        assertEquals(23, ei0_dtz_precomp.symLen.length);


        EncInfo ei1_dtz = dtz.ei[1];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei1_dtz.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei1_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei1_dtz.norm);

        PairsData ei1_dtz_precomp = ei1_dtz.precomp;
        assertEquals(786, ei1_dtz_precomp.indexTable.ptr);
        assertEquals(806, ei1_dtz_precomp.sizeTable.ptr);
        assertEquals(1088, ei1_dtz_precomp.data.ptr);
        assertEquals(116, ei1_dtz_precomp.offset.ptr);
        assertEquals(138, ei1_dtz_precomp.symPat.ptr);

        assertEquals(10, ei1_dtz_precomp.blockSize);
        assertEquals(15, ei1_dtz_precomp.idxBits);
        assertEquals(4, ei1_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xE000000000000000L, 0x7800000000000000L,
                0x2C00000000000000L, 0x400000000000000L,
                0x100000000000000L, 0}, ei1_dtz_precomp.base);
        assertEquals(59, ei1_dtz_precomp.symLen.length);


        EncInfo ei2_dtz = dtz.ei[2];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei2_dtz.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei2_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei2_dtz.norm);

        PairsData ei2_dtz_precomp = ei2_dtz.precomp;
        assertEquals(792, ei2_dtz_precomp.indexTable.ptr);
        assertEquals(808, ei2_dtz_precomp.sizeTable.ptr);
        assertEquals(2112, ei2_dtz_precomp.data.ptr);
        assertEquals(316, ei2_dtz_precomp.offset.ptr);
        assertEquals(338, ei2_dtz_precomp.symPat.ptr);

        assertEquals(10, ei2_dtz_precomp.blockSize);
        assertEquals(15, ei2_dtz_precomp.idxBits);
        assertEquals(5, ei2_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xA800000000000000L, 0x2800000000000000L,
                0x400000000000000L, 0x200000000000000L,
                0}, ei2_dtz_precomp.base);
        assertEquals(67, ei2_dtz_precomp.symLen.length);


        EncInfo ei3_dtz = dtz.ei[3];
        assertArrayEquals(new int[]{3906, 1, 63, 0, 0, 0, 0}, ei3_dtz.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei3_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei3_dtz.norm);

        PairsData ei3_dtz_precomp = ei3_dtz.precomp;
        assertEquals(798, ei3_dtz_precomp.indexTable.ptr);
        assertEquals(810, ei3_dtz_precomp.sizeTable.ptr);
        assertEquals(3136, ei3_dtz_precomp.data.ptr);
        assertEquals(542, ei3_dtz_precomp.offset.ptr);
        assertEquals(564, ei3_dtz_precomp.symPat.ptr);

        assertEquals(10, ei3_dtz_precomp.blockSize);
        assertEquals(15, ei3_dtz_precomp.idxBits);
        assertEquals(4, ei3_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xF000000000000000L, 0x9800000000000000L,
                0x4000000000000000L, 0xA00000000000000L,
                0x100000000000000L, 0}, ei3_dtz_precomp.base);
        assertEquals(72, ei3_dtz_precomp.symLen.length);
    }
}
