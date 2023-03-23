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
public class CastlingBlackQueenMove extends AbstractCastlingMove{

    private static final PiecePositioned FROM = PiecePositioned.getPiecePositioned(Square.e8, Piece.KING_BLACK);
    private static final PiecePositioned TO = PiecePositioned.getPiecePositioned(Square.c8, null);

    private static final PiecePositioned ROOK_FROM = PiecePositioned.getPiecePositioned(Square.a8, Piece.ROOK_BLACK);
    private static final PiecePositioned ROOK_TO = PiecePositioned.getPiecePositioned(Square.d8, null);


    public CastlingBlackQueenMove() {
        super(FROM, TO, ROOK_FROM, ROOK_TO);
    }

    @Override
    public void executeMove(PositionState positionState) {
        positionState.pushState();
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setCastlingBlackQueenAllowed(false);
        positionState.setEnPassantSquare(null);
        positionState.incrementHalfMoveClock();
        positionState.incrementFullMoveClock();
        positionState.rollTurn();
    }

    @Override
    public Move getRookMove() {
        return null;
    }

    @Override
    protected void xorCastling(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        if(oldPositionState.isCastlingBlackKingAllowed() == true){
            hash.xorCastleBlackKing();
        }

        hash.xorCastleBlackQueen();
    }

}
