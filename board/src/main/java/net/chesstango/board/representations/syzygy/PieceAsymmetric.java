package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PieceAsymmetric extends TableBase {
    final EncInfo[] ei;
    final PieceEntry pieceEntry;
    final PieceAlgorithm pieceAlgorithm;

    public PieceAsymmetric(PieceEntry pieceEntry, TableType tableType) {
        super(tableType);
        this.pieceAlgorithm = new PieceAlgorithm(pieceEntry, mappedFile);
        this.pieceEntry = pieceEntry;
        this.ei = new EncInfo[tableType.getEcInfoSizePiece()];
        for (int i = 0; i < ei.length; i++) {
            ei[i] = new EncInfo();
        }
    }

    @Override
    PieceEntry getBaseEntry() {
        return pieceEntry;
    }

    @Override
    boolean init_table_imp() {
        return switch (tableType) {
            case WDL -> pieceAlgorithm.init_table_wdl(this);
            case DTZ -> pieceAlgorithm.init_table_dtz(this);
            default -> false;
        };
    }

}
