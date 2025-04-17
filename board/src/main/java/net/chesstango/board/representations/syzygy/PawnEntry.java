package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.TableType.DTM;

/**
 * @author Mauricio Coria
 */
class PawnEntry extends BaseEntry {
    EncInfo[] eiWDL = new EncInfo[8];
    EncInfo[] eiDTM = new EncInfo[12];
    EncInfo[] eiDTZ = new EncInfo[4];
    char[] pawns = new char[2];

    PawnEntry(Syzygy syzygy) {
        super(syzygy);
    }

    @Override
    protected TableData createTable(TableType tableType) {
        return null;
    }

    @Override
    protected void init_tb(int[] pcs) {
        syzygy.pawnEntry[syzygy.tbNumPawn++] = this;
        // Handle pawn-specific attributes
        this.pawns[0] = (char) pcs[SyzygyConstants.Piece.W_PAWN.getValue()];
        this.pawns[1] = (char) pcs[SyzygyConstants.Piece.B_PAWN.getValue()];
        if (pcs[SyzygyConstants.Piece.B_PAWN.getValue()] != 0 && (pcs[SyzygyConstants.Piece.W_PAWN.getValue()] != 0 || (pcs[SyzygyConstants.Piece.W_PAWN.getValue()] > pcs[SyzygyConstants.Piece.B_PAWN.getValue()]))) {
            char tmp = this.pawns[0];
            this.pawns[0] = this.pawns[1];
            this.pawns[1] = tmp;
        }
    }

    @Override
    int num_tables(TableType type) {
        return DTM.equals(type) ? 6 : 4;
    }

    @Override
    EncInfo[] first_ei(TableType type) {
        return switch (type) {
            case WDL -> eiWDL;
            case DTM -> eiDTM;
            case DTZ -> eiDTZ;
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };
    }
}
