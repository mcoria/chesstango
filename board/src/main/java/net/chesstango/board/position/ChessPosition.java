package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface ChessPosition extends ChessPositionReader {
    void init();
    
	void doMove(ChessPositionVisitor move);

    void undoMove(ChessPositionVisitor move);

    SquareBoard getSquareBoard();

    BitBoard getBitBoard();

    KingSquare getKingSquare();

    MoveCacheBoard getMoveCache();

    PositionState getPositionState();

    ZobristHash getZobrist();
}
