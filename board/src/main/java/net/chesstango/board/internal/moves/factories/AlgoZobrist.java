package net.chesstango.board.internal.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.ZobristHashWriter;

/**
 * @author Mauricio Coria
 */
public class AlgoZobrist {

    public void defaultFnDoZobrist(PiecePositioned from, PiecePositioned to, ZobristHashWriter hash, PositionStateReader currentPositionReader, PositionStateReader previousPositionState) {
        hash.xorPosition(from);

        if(to.piece() != null) {
            hash.xorPosition(to);
        }

        hash.xorPosition(PiecePositioned.of(to.square(), from.piece()));

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
