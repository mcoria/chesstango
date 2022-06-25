package chess.ai.imp.smart;

import chess.ai.BestMoveFinder;
import chess.board.Game;
import chess.board.moves.Move;

import java.util.ArrayList;
import java.util.List;

public class IterativeDeeping extends AbstractSmart {

    private static final int MAX_DEPTH = 2;

    private AbstractSmart imp;

    private final int maxDepth;
    private final List<BestMove> bestMovesByDepth;

    public IterativeDeeping(){
        this(MAX_DEPTH);
    }

    public IterativeDeeping(int maxDepth) {
        this.maxDepth = maxDepth;
        this.bestMovesByDepth = new ArrayList<>();
    }

    @Override
    public Move searchBestMove(Game game) {
        keepProcessing = true;
        bestMovesByDepth.clear();
        for(int i = 2; i <= 2 * maxDepth ; i += 2){

            imp = getBestMoveFinder(i);

            Move move = imp.searchBestMove(game);
            int evaluation = imp.getEvaluation();

            BestMove bestMove = new BestMove(move, evaluation);
            if(keepProcessing){
                bestMovesByDepth.add(bestMove);
                if(GameEvaluator.WHITE_WON == evaluation|| GameEvaluator.BLACK_WON == evaluation){
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

    protected AbstractSmart getBestMoveFinder(int depth) {
        return new MinMaxPruning(depth);
    }

    private static class BestMove{
        public final Move move;
        public final int evaluation;

        public BestMove(Move move, int evaluation) {
            this.move = move;
            this.evaluation = evaluation;
        }
    }
}
