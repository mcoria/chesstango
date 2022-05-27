package chess.ai.imp.smart;

import chess.board.*;
import chess.board.position.ChessPositionReader;

import java.util.Iterator;

public class GameEvaluator {
    public int evaluate(Game game, int depth) {
        int evaluation = 0;
        if (GameState.GameStatus.MATE.equals(game.getGameStatus())) {
            evaluation = Color.BLACK.equals(game.getChessPositionReader().getCurrentTurn()) ? Integer.MAX_VALUE - depth
                    : Integer.MIN_VALUE + depth;
        }  else if (GameState.GameStatus.DRAW.equals(game.getGameStatus())) {
            evaluation = 0;
        }  else if (GameState.GameStatus.CHECK.equals(game.getGameStatus())) {
            evaluation = Color.BLACK.equals(game.getChessPositionReader().getCurrentTurn()) ? 90 - depth : -90 + depth;
        } else {
            ChessPositionReader positionReader = game.getChessPositionReader();
            for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
                PiecePositioned piecePlacement = it.next();
                Piece piece = piecePlacement.getValue();
                evaluation += piece.getValue();
            }
        }
        return evaluation;
    }
}
