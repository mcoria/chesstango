package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.ZobristHashWriter;

/**
 * @author Mauricio Coria
 */
class AlgoZobrist {

    public void defaultFnDoZobrist(PiecePositioned from, PiecePositioned to, ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        hash.pushState();

        hash.xorPosition(from);

        if(to.getPiece() != null) {
            hash.xorPosition(to);
        }

        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));

        if(oldPositionState.isCastlingWhiteKingAllowed() != newPositionState.isCastlingWhiteKingAllowed()){
            hash.xorCastleWhiteKing();
        }

        if(oldPositionState.isCastlingWhiteQueenAllowed() != newPositionState.isCastlingWhiteQueenAllowed()){
            hash.xorCastleWhiteQueen();
        }


        if(oldPositionState.isCastlingBlackKingAllowed() != newPositionState.isCastlingBlackKingAllowed()){
            hash.xorCastleBlackKing();
        }

        if(oldPositionState.isCastlingBlackQueenAllowed() != newPositionState.isCastlingBlackQueenAllowed()){
            hash.xorCastleBlackQueen();
        }

        hash.xorOldEnPassantSquare();

        hash.xorTurn();
    }


}
