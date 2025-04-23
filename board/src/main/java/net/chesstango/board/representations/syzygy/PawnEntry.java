package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;

/**
 * @author Mauricio Coria
 */
class PawnEntry extends BaseEntry {
    char[] pawns = new char[2];

    PawnEntry(Syzygy syzygy) {
        super(syzygy);
    }

    @Override
    protected TableBase createTable(TableType tableType) {
        return this.symmetric ? new PawnSymmetric(this, tableType) : new PawnAsymmetric(this, tableType);
    }

    @Override
    protected void init_tb(int[] pcs) {
        syzygy.pawnEntry[syzygy.tbNumPawn++] = this;
        // Handle pawn-specific attributes
        this.pawns[0] = (char) pcs[Piece.W_PAWN.getValue()];
        this.pawns[1] = (char) pcs[Piece.B_PAWN.getValue()];
        if (pcs[SyzygyConstants.Piece.B_PAWN.getValue()] != 0 && (pcs[Piece.W_PAWN.getValue()] != 0 || (pcs[Piece.W_PAWN.getValue()] > pcs[Piece.B_PAWN.getValue()]))) {
            char tmp = this.pawns[0];
            this.pawns[0] = this.pawns[1];
            this.pawns[1] = tmp;
        }
    }

}
