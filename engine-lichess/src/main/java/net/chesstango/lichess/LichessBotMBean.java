package net.chesstango.lichess;

/**
 * @author Mauricio Coria
 */
public interface LichessBotMBean {
    /**
     * Send challenge request
     *
     * @param user The user or bot name
     * @param type Challenge type
     */
    void challengeUser(String user, String type);

    void challengeRandomBot();

    /**
     * Stop accepting/sending challenges
     */
    void stopAcceptingChallenges();
}
