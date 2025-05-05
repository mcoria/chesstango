package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.gardel.fen.FENBuilder;
import net.chesstango.evaluation.Evaluator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class EvaluatorByFEN implements Evaluator {
    private int defaultValue;
    private final Map<String, Integer> evaluations = new HashMap<>();

    private Game game;

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public int evaluate() {
        return game.getStatus().isFinalStatus() ? evaluateFinalStatus() : evaluateNonFinalStatus();
    }

    protected int evaluateNonFinalStatus() {
        FENBuilder fenBuilder = new FENBuilder();

        game.getPosition().constructChessPositionRepresentation(fenBuilder);

        String fen = fenBuilder.getPositionRepresentation().toString();

        Integer evaluation = evaluations.get(fen);

        return evaluation == null ? defaultValue : evaluation;
    }

    protected int evaluateFinalStatus() {
        return switch (game.getStatus()) {
            case MATE -> Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
            case STALEMATE, DRAW_BY_FIFTY_RULE, DRAW_BY_FOLD_REPETITION -> 0;
            default -> throw new RuntimeException("Game is still in progress");
        };
    }

    public EvaluatorByFEN setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public EvaluatorByFEN addEvaluation(String fen, int evaluation) {
        evaluations.put(fen, evaluation);
        return this;
    }

    public static EvaluatorByFEN loadEvaluations() {
        EvaluatorByFEN mock = new EvaluatorByFEN();
        mock.setDefaultValue(0);
        mock.addEvaluation(FENParser.INITIAL_FEN, 0);

        return mock;
    }
}
