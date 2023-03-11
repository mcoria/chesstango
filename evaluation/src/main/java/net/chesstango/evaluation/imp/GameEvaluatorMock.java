package net.chesstango.evaluation.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.evaluation.GameEvaluator;

import java.util.HashMap;
import java.util.Map;

public class GameEvaluatorMock implements GameEvaluator {

    private int defaultValue;

    private Map<String, Integer> evaluations = new HashMap<>();

    @Override
    public int evaluate(final Game game) {
        return game.getStatus().isFinalStatus() ? evaluateFinalStatus(game) : getEvaluation(game);
    }


    protected int getEvaluation(final Game game) {
        FENEncoder fenEncoder = new FENEncoder();

        game.getChessPosition().constructBoardRepresentation(fenEncoder);

        String fen = fenEncoder.getChessRepresentation();

        Integer evaluation = evaluations.get(fen);

        return evaluation == null ? defaultValue : evaluation.intValue();
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void addEvaluation(String fen, int evaluation) {
        evaluations.put(fen, evaluation);
    }

    public static GameEvaluatorMock loadEvaluations(){
        GameEvaluatorMock mock = new GameEvaluatorMock();
        mock.setDefaultValue(0);
        mock.addEvaluation(FENDecoder.INITIAL_FEN, 0);

        return mock;
    }

    private int evaluateFinalStatus(final Game game) {
        int evaluation = 0;
        switch (game.getStatus()) {
            case MATE:
                // If white is on mate then evaluation is INFINITE_NEGATIVE
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
                break;
            case DRAW:
                evaluation = 0;
                break;
            case CHECK:
            case NO_CHECK:
                throw new RuntimeException("Game is still in progress");
        }
        return evaluation;
    }
}
