package net.chesstango.ai.imp.smart.evaluation;

import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Mauricio Coria
 *
 */
public class GameEvaluator  {

    private static final int FACTOR_MATERIAL_DEFAULT = 365;
    private static final int FACTOR_EXPANSION_DEFAULT = 325;
    private static final int FACTOR_ATAQUE_DEFAULT = 91;

    public static final int INFINITE_POSITIVE = Integer.MAX_VALUE;
    public static final int INFINITE_NEGATIVE = -INFINITE_POSITIVE;

    public static final int WHITE_LOST = INFINITE_NEGATIVE;
    public static final int BLACK_WON = WHITE_LOST;

    public static final int BLACK_LOST = INFINITE_POSITIVE;
    public static final int WHITE_WON = BLACK_LOST;

    private final int material;
    private final int expansion;
    private final int ataque;

    public GameEvaluator(){
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_EXPANSION_DEFAULT, FACTOR_ATAQUE_DEFAULT);
    }

    public GameEvaluator(int material, int expansion, int ataque){
        this.material = material;
        this.expansion =  expansion;
        this.ataque = ataque;
    }

    public int evaluate(final Game game) {
        int evaluation = 0;
        switch (game.getStatus()){
            case MATE:
                // If white is on mate then evaluation is INFINITE_NEGATIVE
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
                break;
            case DRAW:
                evaluation = 0;
                break;
            case  CHECK:
                // If white is on check then evaluation starts at -1
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? -1 : +1;
            case NO_CHECK:
                evaluation += 10 * material * evaluateByMaterial(game);
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
            evaluation += piece.getPieceValue();
        }
        return evaluation;
    }

    protected int evaluateByMoves(final Game game){
        int evaluation = 0;
        Set<Square> origenes = new HashSet<>();
        Set<Square> territorioExpansion = new HashSet<>();
        Set<Square> territorioAtaque = new HashSet<>();
        int posiblesCapturasValor = 0;
        for(Move move: game.getPossibleMoves()){
            origenes.add(move.getFrom().getKey());

            PiecePositioned to = move.getTo();

            territorioExpansion.add(to.getKey());

            if(to.getValue() != null){
                territorioAtaque.add(to.getKey());
                posiblesCapturasValor += Math.abs(to.getValue().getPieceValue());
            }
        };

        evaluation = origenes.size() + expansion * territorioExpansion.size() + ataque * territorioAtaque.size() + posiblesCapturasValor;

        return (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) ? evaluation : - evaluation;
    }

}
