package net.chesstango.search.smart.alphabeta.pv.model;

import net.chesstango.search.smart.Constants;

import static net.chesstango.search.smart.Constants.PV_MAX_DEPTH;

/**
 *
 * @author Mauricio Coria
 */
public class TriangularPVTable {
    short[][] pvTable = new short[PV_MAX_DEPTH][PV_MAX_DEPTH];
    int[] pvLength = new int[PV_MAX_DEPTH];

    public void clearPV(int ply) {
        pvLength[ply] = ply;
    }

    /**
     * Prepend best move at ply, then copy child's PV tail.
     */
    public void updatePV(int ply, short move) {
        pvTable[ply][ply] = move;

        if (pvLength[ply + 1] - (ply + 1) > 0)
            System.arraycopy(pvTable[ply + 1], ply + 1, pvTable[ply], ply + 1, pvLength[ply + 1] - (ply + 1));

        pvLength[ply] = pvLength[ply + 1];
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
