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
public class CastlingWhiteKingMove extends AbstractCastlingMove{

    private static final PiecePositioned FROM = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
    private static final PiecePositioned TO = PiecePositioned.getPiecePositioned(Square.g1, null);

    private static final PiecePositioned ROOK_FROM = PiecePositioned.getPiecePositioned(Square.h1, Piece.ROOK_WHITE);
    private static final PiecePositioned ROOK_TO = PiecePositioned.getPiecePositioned(Square.f1, null);


    public CastlingWhiteKingMove() {
        super(FROM, TO, ROOK_FROM, ROOK_TO);
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
    public Move getRookMove() {
        return null;
    }


    @Override
    protected void xorCastling(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        if(oldPositionState.isCastlingWhiteQueenAllowed() == true){
            hash.xorCastleWhiteQueen();
        }

        hash.xorCastleWhiteKing();
    }
}
