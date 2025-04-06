package net.chesstango.board.internal.moves;

import net.chesstango.board.internal.GameImp;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.StateReader;
import net.chesstango.board.position.StateWriter;
import net.chesstango.board.position.ZobristHashWriter;

/**
 * @author Mauricio Coria
 */
public class MoveCastlingBlackKing extends MoveCastlingImp {

    public static final PiecePositioned KING_FROM = PiecePositioned.of(Square.e8, Piece.KING_BLACK);
    public static final PiecePositioned KING_TO = PiecePositioned.of(Square.g8, null);

    public static final PiecePositioned ROOK_FROM = PiecePositioned.of(Square.h8, Piece.ROOK_BLACK);
    public static final PiecePositioned ROOK_TO = PiecePositioned.of(Square.f8, null);


    public MoveCastlingBlackKing(GameImp gameImp) {
        super(gameImp, KING_FROM, KING_TO, ROOK_FROM, ROOK_TO);
    }

    @Override
    public void doMove(StateWriter positionState) {
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
    protected void xorCastling(ZobristHashWriter hash, StateReader oldPositionState, StateReader newPositionState) {
        if (oldPositionState.isCastlingBlackQueenAllowed()) {
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
