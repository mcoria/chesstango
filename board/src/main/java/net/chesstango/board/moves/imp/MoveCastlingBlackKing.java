package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.PositionStateWriter;
import net.chesstango.board.position.ZobristHashWriter;

/**
 * @author Mauricio Coria
 */
class MoveCastlingBlackKing extends AbstractCastlingMove{

    protected static final PiecePositioned KING_FROM = PiecePositioned.getPiecePositioned(Square.e8, Piece.KING_BLACK);
    protected static final PiecePositioned KING_TO = PiecePositioned.getPiecePositioned(Square.g8, null);

    protected static final PiecePositioned ROOK_FROM = PiecePositioned.getPiecePositioned(Square.h8, Piece.ROOK_BLACK);
    protected static final PiecePositioned ROOK_TO = PiecePositioned.getPiecePositioned(Square.f8, null);


    public MoveCastlingBlackKing() {
        super(KING_FROM, KING_TO, ROOK_FROM, ROOK_TO);
    }

    @Override
    public void executeMove(PositionStateWriter positionState) {
        positionState.pushState();
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setCastlingBlackQueenAllowed(false);
        positionState.setEnPassantSquare(null);
        positionState.incrementHalfMoveClock();
        positionState.incrementFullMoveClock();
        positionState.rollTurn();
    }

    @Override
    public boolean isQuiet() {
        return true;
    }

    @Override
    protected void xorCastling(ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        if(oldPositionState.isCastlingBlackQueenAllowed() == true){
            hash.xorCastleBlackQueen();
        }

        hash.xorCastleBlackKing();
    }

    @Override
    public PiecePositioned getRookFrom() {
        return ROOK_FROM;
    }

    @Override
    public PiecePositioned getRookTo() {
        return ROOK_TO;
    }
}
