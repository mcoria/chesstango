package net.chesstango.board.moves.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 */
public class AlogZobrit {

    public void defaultFnDoZobrit(PiecePositioned from, PiecePositioned to, ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
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
            hash.xorCastleWhiteKing();
        }

        if(oldPositionState.isCastlingBlackQueenAllowed() != newPositionState.isCastlingBlackQueenAllowed()){
            hash.xorCastleWhiteQueen();
        }

        hash.xorTurn();
    }


}
