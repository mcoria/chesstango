package net.chesstango.search.smart.alphabeta.pv.model;

import static net.chesstango.search.smart.Constants.MAX_DEPTH;

/**
 *
 * @author Mauricio Coria
 */
public class TriangularPVTable {
    short[][] pvTable = new short[MAX_DEPTH][MAX_DEPTH];
    int[] pvLength = new int[MAX_DEPTH];

    public void clearPV(int ply) {
        //System.out.printf("%sClearPV %d%n", "\t".repeat(ply), ply);
        pvLength[ply] = ply;
    }

    /**
     * Prepend best move at ply, then copy child's PV tail.
     */
    public void updatePV(int ply, short move) {
        //System.out.printf("%sUpdatePV %d %s%n", "\t".repeat(ply), ply, move);
        pvTable[ply][ply] = move;

        final int nextPly = ply + 1;
        if (pvLength[nextPly] - nextPly > 0) {
            System.arraycopy(pvTable[nextPly], nextPly, pvTable[ply], nextPly, pvLength[nextPly] - nextPly);
        }

        pvLength[ply] = pvLength[nextPly];
    }

    public short[] getRootPV() {
        return getPV(0);
    }

    public short[] getPV(int ply) {
        int len = pvLength[ply];
        short[] pv = new short[len - ply];
        System.arraycopy(pvTable[ply], ply, pv, 0, len - ply);
        return pv;
    }

    public void writePV(int ply, short move) {
        int len = pvLength[ply];
        pvTable[ply][len] = move;
        pvLength[ply]++;
    }
}
