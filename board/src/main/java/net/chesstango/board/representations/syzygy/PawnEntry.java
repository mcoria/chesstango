package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;

/**
 * @author Mauricio Coria
 */
class PawnEntry extends BaseEntry {
    byte[] pawns = new byte[2];

    PawnEntry(Syzygy syzygy) {
        super(syzygy);
    }

    @Override
    protected TableBase createTable(TableBase.TableType tableType) {
        if (tableType == TableBase.TableType.WDL) {
            if (symmetric)
                return new PawnSymmetricWdl(this);
            else
                return new PawnAsymmetricWdl(this);
        }
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    protected void init_tb(int[] pcs) {
        syzygy.pawnEntry[syzygy.tbNumPawn++] = this;
        // Handle pawn-specific attributes
        this.pawns[0] = (byte) pcs[Piece.W_PAWN.getValue()];
        this.pawns[1] = (byte) pcs[Piece.B_PAWN.getValue()];
        if (pcs[SyzygyConstants.Piece.B_PAWN.getValue()] != 0 && (pcs[Piece.W_PAWN.getValue()] != 0 || (pcs[Piece.W_PAWN.getValue()] > pcs[Piece.B_PAWN.getValue()]))) {
            byte tmp = this.pawns[0];
            this.pawns[0] = this.pawns[1];
            this.pawns[1] = tmp;
        }
    }

}
