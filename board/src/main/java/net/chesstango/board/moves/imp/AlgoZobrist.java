package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.ZobristHashWriter;

/**
 * @author Mauricio Coria
 */
public class AlgoZobrist {

    public void defaultFnDoZobrist(PiecePositioned from, PiecePositioned to, ZobristHashWriter hash, ChessPositionReader chessPositionReader) {
        hash.pushState();

        hash.xorPosition(from);

        if(to.getPiece() != null) {
            hash.xorPosition(to);
        }

        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));

        PositionStateReader oldPositionState = chessPositionReader.getPreviousPositionState();

        if(oldPositionState.isCastlingWhiteKingAllowed() != chessPositionReader.isCastlingWhiteKingAllowed()){
            hash.xorCastleWhiteKing();
        }

        if(oldPositionState.isCastlingWhiteQueenAllowed() != chessPositionReader.isCastlingWhiteQueenAllowed()){
            hash.xorCastleWhiteQueen();
        }

        if(oldPositionState.isCastlingBlackKingAllowed() != chessPositionReader.isCastlingBlackKingAllowed()){
            hash.xorCastleBlackKing();
        }

        if(oldPositionState.isCastlingBlackQueenAllowed() != chessPositionReader.isCastlingBlackQueenAllowed()){
            hash.xorCastleBlackQueen();
        }

        hash.clearEnPassantSquare();

        hash.xorTurn();
    }


}
