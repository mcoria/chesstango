package chess.ai.imp.smart;

import chess.board.*;
import chess.board.moves.Move;
import chess.board.position.ChessPositionReader;
import chess.board.representations.MoveEncoder;

import java.util.Iterator;

public class GameEvaluator {
    public int evaluate(Game game, int depth) {
        int evaluation = 0;
        if (GameState.GameStatus.MATE.equals(game.getGameStatus())) {
            evaluation = Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn()) ? Integer.MIN_VALUE + depth: Integer.MAX_VALUE - depth;
            //printGameStack(game);
            //System.out.println(evaluation);
        }  else if (GameState.GameStatus.DRAW.equals(game.getGameStatus())) {
            evaluation = 0;
        }  else if (GameState.GameStatus.CHECK.equals(game.getGameStatus())) {
            evaluation = Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn()) ? - 100  : 100;
            ChessPositionReader positionReader = game.getChessPositionReader();
            for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
                PiecePositioned piecePlacement = it.next();
                Piece piece = piecePlacement.getValue();
                evaluation += piece.getValue();
            }
            evaluation +=  game.getPossibleMoves().size();
        } else {
            ChessPositionReader positionReader = game.getChessPositionReader();
            for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
                PiecePositioned piecePlacement = it.next();
                Piece piece = piecePlacement.getValue();
                evaluation += piece.getValue();
            }
            evaluation =  Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn()) ? evaluation + game.getPossibleMoves().size(): evaluation - game.getPossibleMoves().size();
        }
        return evaluation;
    }

    private void printGameStack(Game game) {
        MoveEncoder moveEncoder = new MoveEncoder();
        GameState currentGameState = game.getGameState();
        Iterator<GameState.GameStateNode> iterator = currentGameState.iterateGameStates();
        while (iterator.hasNext()){
            GameState.GameStateNode state = iterator.next();
            System.out.print(moveEncoder.encode(state.selectedMove) + ", ");
        }
    }
}
