package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.evaluation.GameEvaluator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class EvaluatorByFEN implements GameEvaluator {
    private int defaultValue;
    private Map<String, Integer> evaluations = new HashMap<>();

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
        FENEncoder fenEncoder = new FENEncoder();

        game.getChessPosition().constructChessPositionRepresentation(fenEncoder);

        String fen = fenEncoder.getChessRepresentation();

        Integer evaluation = evaluations.get(fen);

        return evaluation == null ? defaultValue : evaluation.intValue();
    }

    protected int evaluateFinalStatus() {
        return switch (game.getStatus()) {
            case MATE -> Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
            case DRAW, DRAW_BY_FIFTY_RULE, DRAW_BY_FOLD_REPETITION -> 0;
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
        mock.addEvaluation(FENDecoder.INITIAL_FEN, 0);

        return mock;
    }
}
