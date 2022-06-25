package chess.ai.imp.smart;

import chess.ai.BestMoveFinder;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractSmart implements BestMoveFinder {

    protected int evaluation;

    protected boolean keepProcessing = true;


    public int getEvaluation() {
        return this.evaluation;
    }

    @Override
    public void stopSearching() {
        keepProcessing = false;
    }

}
