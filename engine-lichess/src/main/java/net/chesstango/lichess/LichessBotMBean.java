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


    /**
     * Send challenge request to a random bot with rating within acceptable threshold
     */
    void challengeRandomBot();

    /**
     * Stop accepting/sending challenges
     */
    void stopAcceptingChallenges();
}
