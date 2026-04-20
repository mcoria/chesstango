package net.chesstango.search.smart.alphabeta.pv.model;

/**
 *
 * @author Mauricio Coria
 */
public class TriangularPVTable {
    static final int MAX_DEPTH = 64;

    short[][] pvTable = new short[MAX_DEPTH][MAX_DEPTH];
    int[] pvLength = new int[MAX_DEPTH];

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
}
