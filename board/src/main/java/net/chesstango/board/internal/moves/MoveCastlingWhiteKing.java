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
public class MoveCastlingWhiteKing extends MoveCastlingImp {

    public static final PiecePositioned KING_FROM = PiecePositioned.of(Square.e1, Piece.KING_WHITE);
    public static final PiecePositioned KING_TO = PiecePositioned.of(Square.g1, null);

    public static final PiecePositioned ROOK_FROM = PiecePositioned.of(Square.h1, Piece.ROOK_WHITE);
    public static final PiecePositioned ROOK_TO = PiecePositioned.of(Square.f1, null);


    public MoveCastlingWhiteKing(GameImp gameImp) {
        super(gameImp, KING_FROM, KING_TO, ROOK_FROM, ROOK_TO);
    }

    @Override
    public void doMove(StateWriter positionState) {
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
    protected void xorCastling(ZobristHashWriter hash, StateReader oldPositionState, StateReader newPositionState) {
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
