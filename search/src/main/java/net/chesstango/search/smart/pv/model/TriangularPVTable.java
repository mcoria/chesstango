package net.chesstango.search.smart.pv.model;

import net.chesstango.board.moves.Move;

import static net.chesstango.search.smart.Constants.MAX_DEPTH;

/**
 *
 * @author Mauricio Coria
 */
public class TriangularPVTable {
    Move[][] pvTable = new Move[MAX_DEPTH][MAX_DEPTH];
    int[] pvLength = new int[MAX_DEPTH];

    public void extendLine(int ply, Move move) {
        //System.out.printf("%sClearPV  %d%n", "\t".repeat(ply), ply);
        pvLength[ply] = ply;
        pvTable[ply][ply] = move;
    }

    /**
     * Prepend best move at ply, then copy child's PV tail.
     */
    public void propagateLine(int ply) {
        //System.out.printf("%sUpdatePV %d %n", "\t".repeat(ply), ply);

        final int nextPly = ply + 1;
        if (pvLength[nextPly] - ply > 0) {
            System.arraycopy(
                    pvTable[nextPly],   // src
                    nextPly,            // srcPos
                    pvTable[ply],       // dst
                    nextPly,            // dstPos
                    pvLength[nextPly] - ply     // length
            );
        }

        pvLength[ply] = pvLength[nextPly];
    }

    public Move[] getRootPV() {
        return getPV(0);
    }

    public Move[] getPV(int ply) {
        int length = pvLength[ply] + 1 - ply;
        Move[] pv = new Move[length];
        System.arraycopy(
                pvTable[ply],       // src
                ply,                // srcPos
                pv,                 // dst
                0,                  // dstPos
                length              // length
        );
        return pv;
    }
}
