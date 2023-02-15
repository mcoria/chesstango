package net.chesstango.search.smart;

import net.chesstango.search.SearchMove;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractSmart implements SearchMove {

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
