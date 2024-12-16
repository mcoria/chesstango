package net.chesstango.lichess;

/**
 * @author Mauricio Coria
 */
public interface LichessBotMainMBean {
    void challengeUser(String user, String type);

    /**
     * Stop accepting/sending challenges
     */
    void stopChallengingBots();
}
