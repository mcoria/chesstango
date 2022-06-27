package net.chesstango.ai.imp.smart;

import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class IterativeDeeping extends AbstractSmart {

    private final AbstractSmart imp;

    private final List<BestMove> bestMovesByDepth;


    public IterativeDeeping() {
        this(new MinMaxPruning());
    }

    public IterativeDeeping(AbstractSmart minMax) {
        this.bestMovesByDepth = new ArrayList<>();
        this.imp = minMax;
    }

    @Override
    public Move searchBestMove(Game game, int depth) {
        keepProcessing = true;
        bestMovesByDepth.clear();
        for(int i = 1; i <=  depth ; i++){

            Move move = imp.searchBestMove(game, i);
            int evaluation = imp.getEvaluation();

            BestMove bestMove = new BestMove(move, evaluation);
            if(keepProcessing){
                bestMovesByDepth.add(bestMove);
                if(GameEvaluator.WHITE_WON == evaluation || GameEvaluator.BLACK_WON == evaluation){
                    break;
                }
            } else {
                BestMove lastBestMove = bestMovesByDepth.get(bestMovesByDepth.size() - 1);
                if(evaluation >= lastBestMove.evaluation){
                    bestMovesByDepth.add(bestMove);
                }
                break;
            }
        }

        return bestMovesByDepth.get(bestMovesByDepth.size() - 1).move;
    }

    @Override
    public void stopSearching() {
       super.stopSearching();
       imp.stopSearching();
    }

    @Override
    public int getEvaluation() {
        return bestMovesByDepth.get(bestMovesByDepth.size() - 1).evaluation;
    }


    private static class BestMove {
        public final Move move;
        public final int evaluation;

        public BestMove(Move move, int evaluation) {
            this.move = move;
            this.evaluation = evaluation;
        }
    }
}
