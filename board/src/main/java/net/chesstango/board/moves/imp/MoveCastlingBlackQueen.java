package net.chesstango.board.moves.imp;

import net.chesstango.board.GameImp;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.PositionStateWriter;
import net.chesstango.board.position.ZobristHashWriter;

/**
 * @author Mauricio Coria
 */
public class MoveCastlingBlackQueen extends MoveCastlingImp {

    public static final PiecePositioned KING_FROM = PiecePositioned.of(Square.e8, Piece.KING_BLACK);
    public static final PiecePositioned KING_TO = PiecePositioned.of(Square.c8, null);

    public static final PiecePositioned ROOK_FROM = PiecePositioned.of(Square.a8, Piece.ROOK_BLACK);
    public static final PiecePositioned ROOK_TO = PiecePositioned.of(Square.d8, null);


    public MoveCastlingBlackQueen(GameImp gameImp) {
        super(gameImp, KING_FROM, KING_TO, ROOK_FROM, ROOK_TO);
    }

    @Override
    public void doMove(PositionStateWriter positionState) {
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
        if(oldPositionState.isCastlingBlackKingAllowed()){
            hash.xorCastleBlackKing();
        }

        hash.xorCastleBlackQueen();
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
