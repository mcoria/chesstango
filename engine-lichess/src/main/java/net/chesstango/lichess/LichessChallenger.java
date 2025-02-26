package net.chesstango.lichess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mauricio Coria
 */
public class LichessChallenger {

    private static final Logger logger = LoggerFactory.getLogger(LichessChallenger.class);

    private final LichessChallengerBot lichessChallengerBot;
    private final LichessChallengerUser lichessChallengerUser;

    public LichessChallenger(LichessClient client) {
        lichessChallengerBot = new LichessChallengerBot(client);
        lichessChallengerUser = new LichessChallengerUser(client);
    }


    public void challengeRandomBot() {
        lichessChallengerBot.challengeRandomBot();
    }


    public void challengeUser(String username, ChallengeType challengeType) {
        lichessChallengerUser.challengeUser(username, challengeType);
    }
}
