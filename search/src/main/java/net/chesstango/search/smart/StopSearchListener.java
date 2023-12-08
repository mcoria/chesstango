package net.chesstango.search.smart;

/**
 * @author Mauricio Coria
 */
public interface StopSearchListener extends SmartListener{
    /**
     * Stop searching. This method may be called while another thread is searching
     */
    void stopSearching();
}
