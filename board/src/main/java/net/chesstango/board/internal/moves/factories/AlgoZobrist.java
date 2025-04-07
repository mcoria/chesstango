package net.chesstango.board.internal.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.ZobristHashWriter;

/**
 * @author Mauricio Coria
 */
public class AlgoZobrist {

    public void defaultFnDoZobrist(PiecePositioned from, PiecePositioned to, ZobristHashWriter hash, PositionStateReader currentPositionReader, PositionStateReader previousPositionState) {
        hash.pushState();

        hash.xorPosition(from);

        if(to.getPiece() != null) {
            hash.xorPosition(to);
        }

        hash.xorPosition(PiecePositioned.of(to.getSquare(), from.getPiece()));

        if(previousPositionState.isCastlingWhiteKingAllowed() != currentPositionReader.isCastlingWhiteKingAllowed()){
            hash.xorCastleWhiteKing();
        }

        if(previousPositionState.isCastlingWhiteQueenAllowed() != currentPositionReader.isCastlingWhiteQueenAllowed()){
            hash.xorCastleWhiteQueen();
        }

        if(previousPositionState.isCastlingBlackKingAllowed() != currentPositionReader.isCastlingBlackKingAllowed()){
            hash.xorCastleBlackKing();
        }

        if(previousPositionState.isCastlingBlackQueenAllowed() != currentPositionReader.isCastlingBlackQueenAllowed()){
            hash.xorCastleBlackQueen();
        }

        hash.clearEnPassantSquare();

        hash.xorTurn();
    }


}
