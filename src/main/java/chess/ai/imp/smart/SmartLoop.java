package chess.ai.imp.smart;

import chess.ai.BestMoveFinder;
import chess.board.Game;
import chess.board.moves.Move;

import java.util.ArrayList;
import java.util.List;

public class SmartLoop implements BestMoveFinder {

    private static final int MAX_DEPTH = 2;

    private BestMoveFinder imp;

    private boolean stopped = false;

    private final int maxDepth;
    private final List<BestMove> bestMovesByDepth;

    public SmartLoop(){
        this(MAX_DEPTH);
    }

    public SmartLoop(int maxDepth) {
        this.maxDepth = maxDepth;
        this.bestMovesByDepth = new ArrayList<>();
    }

    @Override
    public Move findBestMove(Game game) {
        stopped = false;
        bestMovesByDepth.clear();
        for(int i = 2; i <= 2 * maxDepth ; i += 2){

            imp = getBestMoveFinder(i);

            Move move = imp.findBestMove(game);
            int evaluation = imp.getEvaluation();

            BestMove bestMove = new BestMove(move, evaluation);
            if(stopped){
                BestMove lastBestMove = bestMovesByDepth.get(bestMovesByDepth.size() - 1);
                if(evaluation >= lastBestMove.evaluation){
                    bestMovesByDepth.add(bestMove);
                }
            } else {
                bestMovesByDepth.add(bestMove);
                if(GameEvaluator.WHITE_WON == evaluation|| GameEvaluator.BLACK_WON == evaluation){
                    break;
                }
            }
        }

        return bestMovesByDepth.get(bestMovesByDepth.size() - 1).move;
    }

    @Override
    public void stopProcessing() {
        this.stopped = true;
        imp.stopProcessing();
    }

    @Override
    public int getEvaluation() {
        return bestMovesByDepth.get(bestMovesByDepth.size() - 1).evaluation;
    }

    protected BestMoveFinder getBestMoveFinder(int depth) {
        return new MinMaxPrunning(depth);
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
