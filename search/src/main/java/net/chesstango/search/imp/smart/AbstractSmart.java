package net.chesstango.search.imp.smart;

import net.chesstango.search.BestMoveFinder;

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
