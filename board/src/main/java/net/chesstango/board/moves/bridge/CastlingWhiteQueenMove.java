package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 */
public class CastlingWhiteQueenMove extends AbstractCastlingMove{

    protected static final PiecePositioned KING_FROM = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
    protected static final PiecePositioned KING_TO = PiecePositioned.getPiecePositioned(Square.c1, null);

    protected static final PiecePositioned ROOK_FROM = PiecePositioned.getPiecePositioned(Square.a1, Piece.ROOK_WHITE);
    protected static final PiecePositioned ROOK_TO = PiecePositioned.getPiecePositioned(Square.d1, null);


    public CastlingWhiteQueenMove() {
        super(KING_FROM, KING_TO, ROOK_FROM, ROOK_TO);
    }

    @Override
    public void executeMove(PositionState positionState) {
        positionState.pushState();
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.setEnPassantSquare(null);
        positionState.incrementHalfMoveClock();
        positionState.rollTurn();
    }

    @Override
    protected void xorCastling(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        if(oldPositionState.isCastlingWhiteKingAllowed() == true){
            hash.xorCastleWhiteKing();
        }

        hash.xorCastleWhiteQueen();
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