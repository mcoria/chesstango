package net.chesstango.board.moves.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ZobristHash;

public class AlogZobrit {

    public void defaultFnDoZobrit(PiecePositioned from, PiecePositioned to, ZobristHash hash, PositionStateReader positionStateReader, PositionStateReader positionStateReader1) {
        hash.xorPosition(from);
        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));
        hash.xorTurn();
    }

    public void captureFnDoZobrit(PiecePositioned from, PiecePositioned to, ZobristHash hash, PositionStateReader positionStateReader, PositionStateReader positionStateReader1) {
        hash.xorPosition(from);
        hash.xorPosition(to);
        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));
        hash.xorTurn();
    }
}
