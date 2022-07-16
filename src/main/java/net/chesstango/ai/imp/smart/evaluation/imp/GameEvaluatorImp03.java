package net.chesstango.ai.imp.smart.evaluation.imp;

import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorImp03 implements GameEvaluator {

    private static final int FACTOR_MATERIAL_DEFAULT = 2110;
    private static final int FACTOR_MATERIAL_COLOR_DEFAULT = 601;

    private final int material;
    private final int material_color;

    public GameEvaluatorImp03() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_MATERIAL_COLOR_DEFAULT);
    }

    public GameEvaluatorImp03(int material, int material_color) {
        this.material = material;
        this.material_color = material_color;
    }

    @Override
    public int evaluate(final Game game) {
        int evaluation = 0;
        switch (game.getStatus()) {
            case MATE:
            case DRAW:
                evaluation = GameEvaluator.evaluateFinalStatus(game);
                break;
            case CHECK:
                // If white is on check then evaluation starts at -1
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? -1 : +1;
            case NO_CHECK:
                evaluation += material * GameEvaluator.evaluateByMaterial(game);
                evaluation += material_color * evaluateByColor(game);
        }
        return evaluation;
    }

    protected int evaluateByColor(final Game game) {
        int evaluation = 0;
        ChessPositionReader positionReader = game.getChessPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            evaluation += Color.WHITE.equals(piecePlacement.getValue().getColor()) ? +1 : -1;
        }
        return evaluation;
    }

}
