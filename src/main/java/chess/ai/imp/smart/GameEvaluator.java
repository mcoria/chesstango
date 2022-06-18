package chess.ai.imp.smart;

import chess.board.*;
import chess.board.moves.Move;
import chess.board.position.ChessPositionReader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GameEvaluator {

    public static final int INFINITE_POSITIVE = Integer.MAX_VALUE;
    public static final int INFINITE_NEGATIVE = -INFINITE_POSITIVE;

    public static final int WHITE_LOST = INFINITE_NEGATIVE;
    public static final int BLACK_WON = WHITE_LOST;

    public static final int BLACK_LOST = INFINITE_POSITIVE;
    public static final int WHITE_WON = BLACK_LOST;


    public int evaluate(final Game game) {
        int evaluation = 0;
        switch (game.getGameStatus()){
            case MATE:
                // If white is on mate then evaluation is INFINITE_NEGATIVE
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
                break;
            case DRAW:
                evaluation = 0;
                break;
            case  CHECK:
                // If white is on check then evaluation starts at -5
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? -1 : +1;
            case IN_PROGRESS:
                evaluation += evaluateByMaterial(game);
                evaluation += evaluateByMoves(game);
        }
        return evaluation;
    }

    protected int evaluateByMaterial(final Game game) {
        int evaluation = 0;
        ChessPositionReader positionReader = game.getChessPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            Piece piece = piecePlacement.getValue();
            evaluation += piece.getValue();
        }
        return evaluation;
    }

    protected int evaluateByMoves(final Game game){
        int evaluation = 0;
        Set<Square> origenes = new HashSet<>();
        Set<Square> territorioExpansion = new HashSet<>();
        Set<Square> territorioAtaque = new HashSet<>();
        for(Move move: game.getPossibleMoves()){
            origenes.add(move.getFrom().getKey());

            PiecePositioned to = move.getTo();
            territorioExpansion.add(to.getKey());
            if(to.getValue() != null){
                territorioAtaque.add(to.getKey());
            }
        };

        evaluation = 2 * origenes.size() + territorioExpansion.size() + 2 * territorioAtaque.size();

        return (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) ? evaluation : - evaluation;
    }

}
