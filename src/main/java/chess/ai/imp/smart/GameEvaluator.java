package chess.ai.imp.smart;

import chess.board.*;
import chess.board.moves.Move;
import chess.board.position.ChessPositionReader;
import chess.board.representations.MoveEncoder;

import java.util.Iterator;

public class GameEvaluator {

    public static final int INFINITE_POSITIVE = Integer.MAX_VALUE;
    public static final int INFINITE_NEGATIVE = -INFINITE_POSITIVE;

    public static final int WHITE_LOST = INFINITE_NEGATIVE + 1;
    public static final int BLACK_WON = WHITE_LOST;

    public static final int BLACK_LOST = INFINITE_POSITIVE - 1;
    public static final int WHITE_WON = BLACK_LOST;


    public int evaluate(Game game) {
        int evaluation = 0;
        switch (game.getGameStatus()){
            case MATE:
                // If white is on mate then evaluation is INFINITE_NEGATIVE
                evaluation = Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
                break;
            case DRAW:
                evaluation = 0;
                break;
            case  CHECK:
                // If white is on check then evaluation starts at -100
                evaluation = Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn()) ? -5 : +5;
            case IN_PROGRESS:
                ChessPositionReader positionReader = game.getChessPositionReader();
                for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
                    PiecePositioned piecePlacement = it.next();
                    Piece piece = piecePlacement.getValue();
                    evaluation += piece.getValue();
                }
                //evaluation += Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn()) ? game.getPossibleMoves().size() : - game.getPossibleMoves().size() ;
        }
        return evaluation;
    }

}
