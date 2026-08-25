package net.chesstango.search.smart.pv.model;

import net.chesstango.board.moves.Move;

import static net.chesstango.search.smart.Constants.MAX_DEPTH;

/**
 *
 * @author Mauricio Coria
 */
public class PVTable {
    Move[][] pvTable = new Move[MAX_DEPTH][MAX_DEPTH];
    int[] pvLength = new int[MAX_DEPTH];


    /**
     * Extends the principal variation line at the specified ply by adding a move.
     * <p>
     * This method initializes or resets the PV line at the given ply level by setting
     * the length to the ply value and storing the provided move at the diagonal position
     * in the triangular table. This effectively starts a new PV line from this ply.
     * </p>
     * <p>
     * The method is typically called when a new best move is found at a particular
     * search depth, before propagating the continuation from deeper plies.
     * </p>
     *
     * @param ply  the current ply (depth level) at which to extend the PV line
     * @param move the move to add at this ply position
     */
    public void extendLine(int ply, Move move) {
        //System.out.printf("%sClearPV  %d%n", "\t".repeat(ply), ply);
        pvLength[ply] = ply;
        pvTable[ply][ply] = move;
    }

    /**
     * Propagates the principal variation (PV) line from the next ply to the current ply.
     * <p>
     * This method copies the move sequence from the triangular table at position (ply + 1)
     * to position (ply), effectively bringing the best continuation line from the deeper
     * search level to the current level. The propagation only occurs if there are moves
     * to copy from the next ply.
     * </p>
     * <p>
     * The method updates both the move array (pvTable) and the length tracker (pvLength)
     * for the current ply to reflect the propagated principal variation.
     * </p>
     *
     * @param ply the current ply (depth level) from which to propagate the PV line
     */
    public void propagatePrincipalVariation(int ply) {
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
