package net.chesstango.li;

/**
 * @author Mauricio Coria
 */
public interface LichessBotMainMBean {
    void challengeUser(String user, String type);
    void stopChallengingBots();
}
