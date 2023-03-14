package net.chesstango.search.smart;

import net.chesstango.search.SearchMove;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractSmart implements SearchMove {
    protected boolean keepProcessing = true;

    @Override
    public void stopSearching() {
        keepProcessing = false;
    }

}
