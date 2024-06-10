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
class MoveCastlingWhiteKing extends MoveCastlingImp {

    protected static final PiecePositioned KING_FROM = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
    protected static final PiecePositioned KING_TO = PiecePositioned.getPiecePositioned(Square.g1, null);

    protected static final PiecePositioned ROOK_FROM = PiecePositioned.getPiecePositioned(Square.h1, Piece.ROOK_WHITE);
    protected static final PiecePositioned ROOK_TO = PiecePositioned.getPiecePositioned(Square.f1, null);


    public MoveCastlingWhiteKing() {
        super(KING_FROM, KING_TO, ROOK_FROM, ROOK_TO);
    }

    @Override
    public void doMove(PositionStateWriter positionState) {
        positionState.pushState();
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setEnPassantSquare(null);
        positionState.incrementHalfMoveClock();
        positionState.rollTurn();
    }

    @Override
    public boolean isQuiet() {
        return true;
    }

    @Override
    protected void xorCastling(ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        if(oldPositionState.isCastlingWhiteQueenAllowed()){
            hash.xorCastleWhiteQueen();
        }

        hash.xorCastleWhiteKing();
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
